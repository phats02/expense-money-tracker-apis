package com.ferb.expenseMoneyTracker.client;

import com.ferb.expenseMoneyTracker.entity.Category;
import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.entity.Wallet;
import com.ferb.expenseMoneyTracker.service.CategoryService;
import com.ferb.expenseMoneyTracker.service.WalletService;
import com.google.genai.Client;
import com.google.genai.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GeminiClient {
    private final Client client;
    private final String modelName = "gemini-2.5-flash";

    @Autowired
    CategoryService categoryService;

    @Autowired
    WalletService walletService;

    public GeminiClient() {
        try {
            this.client = new Client();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to initialize Gemini Client", e);
        }
    }

    public GenerateContentResponse generateContent(String prompt) {
        return client.models.generateContent(modelName, prompt, null);
    }

    /**
     * Generate content with function calling support
     * @param prompt The user's message
     * @param tools List of tools (function declarations)
     * @param systemInstruction System instruction for the model
     * @return GenerateContentResponse with potential function calls
     */
    public GenerateContentResponse generateContentWithTools(String prompt, List<Tool> tools, String systemInstruction) {
        GenerateContentConfig config = GenerateContentConfig.builder()
                .tools(tools)
                .systemInstruction(Content.fromParts(Part.fromText(systemInstruction)))
                .build();

        return client.models.generateContent(modelName, prompt, config);
    }

    /**
     * Create a function declaration for the transaction creation tool
     */
    public FunctionDeclaration createTransactionFunctionDeclaration(
            List<Category> categories,
            List<Wallet> wallets
    ) {
        // Build enum values for categoryId and walletId based on user's data
        StringBuilder categoryDescription = new StringBuilder("The UUID of the category. Available categories: ");
        for (Category cat : categories) {
            categoryDescription.append("\n- ").append(cat.getId())
                    .append(" (").append(cat.getType()).append("): ")
                    .append(cat.getTitle());
        }

        StringBuilder walletDescription = new StringBuilder("The UUID of the wallet. Available wallets: ");
        for (Wallet wallet : wallets) {
            walletDescription.append("\n- ").append(wallet.getId())
                    .append(": ").append(wallet.getTitle());
        }

        return FunctionDeclaration.builder()
                .name("create_transaction")
                .description("Creates a financial transaction (expense or income) in the user's expense tracker. " +
                        "Use this when the user mentions spending money, receiving money, purchases, payments, income, etc.")
                .parameters(Schema.builder()
                        .type("object")
                        .properties(Map.of(
                                "title", Schema.builder()
                                        .type("string")
                                        .description("A short, descriptive title for the transaction (e.g., 'Grocery shopping', 'Salary payment', 'Coffee')")
                                        .build(),
                                "amount", Schema.builder()
                                        .type("number")
                                        .description("The transaction amount as a positive number (e.g., 50.00, 1500)")
                                        .build(),
                                "date", Schema.builder()
                                        .type("string")
                                        .description("The date of the transaction in YYYY-MM-DD format. Use today's date if not specified.")
                                        .build(),
                                "categoryId", Schema.builder()
                                        .type("string")
                                        .description(categoryDescription.toString())
                                        .build(),
                                "walletId", Schema.builder()
                                        .type("string")
                                        .description(walletDescription.toString())
                                        .build(),
                                "note", Schema.builder()
                                        .type("string")
                                        .description("Optional additional notes or details about the transaction")
                                        .build()
                        ))
                        .required(List.of("title", "amount", "date", "categoryId", "walletId"))
                        .build())
                .build();
    }

    public Tool buildAllTools(User user){
        List<Category> categories = categoryService.getByOwnerEmail(user);
        List<Wallet> wallets = walletService.findByOwnerId(user);

        if (categories.isEmpty() || wallets.isEmpty()) return null;

        return Tool.builder().functionDeclarations(List.of(
                createTransactionFunctionDeclaration(categories, wallets)))
                .build();
    }

    public void close() {
        client.close();
    }
}
