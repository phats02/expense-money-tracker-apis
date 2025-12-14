package com.ferb.expenseMoneyTracker.service;

import com.ferb.expenseMoneyTracker.client.GeminiClient;
import com.ferb.expenseMoneyTracker.dto.AITransactionResponse;
import com.ferb.expenseMoneyTracker.dto.CreateTransactionRequest;
import com.ferb.expenseMoneyTracker.entity.Category;
import com.ferb.expenseMoneyTracker.entity.Transaction;
import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.entity.Wallet;
import com.google.genai.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class AIChatService {
    @Autowired
    private GeminiClient geminiClient;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WalletService walletService;

    /**
     * Chat endpoint that supports both regular conversation and transaction creation via Function Calling.
     * If the user's message indicates they want to create a transaction, Gemini will call the create_transaction function.
     * Otherwise, it will respond with regular text.
     */
    public AITransactionResponse chat(String prompt, User owner) {
        // Get user's categories and wallets to provide as function context
        List<Category> categories = categoryService.getByOwnerEmail(owner);
        List<Wallet> wallets = walletService.findByOwnerId(owner);

        // If no wallets or categories, can only do regular chat
        if (wallets.isEmpty() || categories.isEmpty()) {
            String response = geminiClient.generateContent(prompt).text();
            return AITransactionResponse.builder()
                    .success(true)
                    .message(response)
                    .functionCalled(false)
                    .build();
        }

        // Build category and wallet data for function declaration
        List<Map<String, String>> categoryData = categories.stream()
                .map(cat -> Map.of(
                        "id", cat.getId().toString(),
                        "type", cat.getType().name(),
                        "title", cat.getTitle()
                ))
                .toList();

        List<Map<String, String>> walletData = wallets.stream()
                .map(w -> Map.of(
                        "id", w.getId().toString(),
                        "title", w.getTitle()
                ))
                .toList();

        // Create the function declaration with user's actual categories and wallets
        FunctionDeclaration createTransactionFn = GeminiClient.createTransactionFunctionDeclaration(categoryData, walletData);

        // Create tool with the function declaration
        Tool transactionTool = Tool.builder()
                .functionDeclarations(List.of(createTransactionFn))
                .build();

        // System instruction to guide the model
        String systemInstruction = buildSystemInstruction();

        // Call Gemini with function calling support
        GenerateContentResponse response = geminiClient.generateContentWithTools(
                prompt,
                List.of(transactionTool),
                systemInstruction
        );

        // Check if the model wants to call a function
        return processResponse(response, owner, categories, wallets);
    }

    private String buildSystemInstruction() {
        return """
            You are a helpful financial assistant for an expense tracking application.
            Today's date is: %s
            
            Your capabilities:
            1. Create transactions when users mention spending money, receiving income, purchases, payments, etc.
            2. Answer general questions about finance and budgeting.
            3. Have friendly conversations.
            
            When users describe a financial transaction (e.g., "I spent $50 on groceries", "Got paid $3000", "Bought coffee for $5"):
            - Use the create_transaction function to record it
            - Match the transaction to the most appropriate category from the user's available categories
            - Use the most appropriate wallet (usually the first/default one if not specified)
            - Parse dates naturally (today, yesterday, last Friday, etc.)
            - Extract the amount as a positive number
            
            For non-transaction messages, respond conversationally without calling any function.
            """.formatted(LocalDate.now().toString());
    }

    private AITransactionResponse processResponse(GenerateContentResponse response, User owner,
                                                   List<Category> categories, List<Wallet> wallets) {
        // Check if there are any candidates
        Optional<List<Candidate>> candidatesOpt = response.candidates();
        if (candidatesOpt.isEmpty() || candidatesOpt.get().isEmpty()) {
            return AITransactionResponse.builder()
                    .success(false)
                    .message("No response from AI")
                    .functionCalled(false)
                    .build();
        }

        Candidate candidate = candidatesOpt.get().get(0);
        Optional<Content> contentOpt = candidate.content();

        if (contentOpt.isEmpty()) {
            return AITransactionResponse.builder()
                    .success(false)
                    .message("Empty response from AI")
                    .functionCalled(false)
                    .build();
        }

        Content content = contentOpt.get();
        Optional<List<Part>> partsOpt = content.parts();

        if (partsOpt.isEmpty() || partsOpt.get().isEmpty()) {
            return AITransactionResponse.builder()
                    .success(false)
                    .message("Empty response from AI")
                    .functionCalled(false)
                    .build();
        }

        // Check each part for function calls or text
        for (Part part : partsOpt.get()) {
            // Check if this part contains a function call
            if (part.functionCall().isPresent()) {
                FunctionCall functionCall = part.functionCall().get();
                return handleFunctionCall(functionCall, owner, categories, wallets);
            }

            // If it's a text response
            if (part.text().isPresent()) {
                return AITransactionResponse.builder()
                        .success(true)
                        .message(part.text().get())
                        .functionCalled(false)
                        .build();
            }
        }

        return AITransactionResponse.builder()
                .success(false)
                .message("Unexpected response format from AI")
                .functionCalled(false)
                .build();
    }

    private AITransactionResponse handleFunctionCall(FunctionCall functionCall, User owner,
                                                      List<Category> categories, List<Wallet> wallets) {
        String functionName = functionCall.name().orElse("");

        if (!"create_transaction".equals(functionName)) {
            return AITransactionResponse.builder()
                    .success(false)
                    .message("Unknown function: " + functionName)
                    .functionCalled(true)
                    .build();
        }

        try {
            // Extract arguments from the function call
            Map<String, Object> args = functionCall.args().orElse(new HashMap<>());

            String title = getStringArg(args, "title");
            BigDecimal amount = getDecimalArg(args, "amount");
            LocalDate date = parseDateArg(args, "date");
            UUID categoryId = UUID.fromString(getStringArg(args, "categoryId"));
            UUID walletId = UUID.fromString(getStringArg(args, "walletId"));
            String note = args.containsKey("note") ? getStringArg(args, "note") : null;

            // Find category and wallet names for the response
            String categoryName = categories.stream()
                    .filter(c -> c.getId().equals(categoryId))
                    .map(Category::getTitle)
                    .findFirst()
                    .orElse("Unknown");

            String walletName = wallets.stream()
                    .filter(w -> w.getId().equals(walletId))
                    .map(Wallet::getTitle)
                    .findFirst()
                    .orElse("Unknown");

            // Create the transaction request
            CreateTransactionRequest request = new CreateTransactionRequest();
            request.setTitle(title);
            request.setAmount(amount);
            request.setDate(date);
            request.setCategoryId(categoryId);
            request.setWalletId(walletId);
            request.setNote(note);

            // Create the transaction
            Transaction transaction = transactionService.createNewTransaction(owner, request);

            // Build parsed data for response
            AITransactionResponse.ParsedTransactionData parsedData = AITransactionResponse.ParsedTransactionData.builder()
                    .title(title)
                    .amount(amount.toString())
                    .date(date.toString())
                    .categoryName(categoryName)
                    .walletName(walletName)
                    .note(note)
                    .build();

            return AITransactionResponse.builder()
                    .success(true)
                    .message("✅ Transaction created: " + title + " - $" + amount + " (" + categoryName + ")")
                    .transaction(transaction)
                    .parsedData(parsedData)
                    .functionCalled(true)
                    .functionName("create_transaction")
                    .build();

        } catch (Exception e) {
            return AITransactionResponse.builder()
                    .success(false)
                    .message("Failed to create transaction: " + e.getMessage())
                    .functionCalled(true)
                    .functionName("create_transaction")
                    .build();
        }
    }

    private String getStringArg(Map<String, Object> args, String key) {
        Object value = args.get(key);
        return value != null ? value.toString() : "";
    }

    private BigDecimal getDecimalArg(Map<String, Object> args, String key) {
        Object value = args.get(key);
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return new BigDecimal(value.toString());
    }

    private LocalDate parseDateArg(Map<String, Object> args, String key) {
        String dateStr = getStringArg(args, key);
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            // Try other common formats
            String[] formats = {"yyyy-MM-dd", "MM/dd/yyyy", "dd/MM/yyyy", "yyyy/MM/dd"};
            for (String format : formats) {
                try {
                    return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(format));
                } catch (DateTimeParseException ignored) {}
            }
            return LocalDate.now();
        }
    }
}
