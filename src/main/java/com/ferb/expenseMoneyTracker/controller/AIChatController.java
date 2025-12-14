package com.ferb.expenseMoneyTracker.controller;

import com.ferb.expenseMoneyTracker.dto.AITransactionResponse;
import com.ferb.expenseMoneyTracker.dto.CustomUserDetail;
import com.ferb.expenseMoneyTracker.service.AIChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "AI features")
public class AIChatController {
    @Autowired
    AIChatService aiChatService;

    @PostMapping("/chat")
    @Operation(
            summary = "Chat with AI (supports Function Calling)",
            description = """
                    Chat with the AI assistant. The AI can:
                    - Create transactions automatically when you describe spending/income
                    - Answer general finance questions
                    - Have regular conversations
                    
                    Examples that trigger transaction creation:
                    - "I spent $50 on groceries yesterday"
                    - "Got paid $3000 salary"
                    - "Coffee $5"
                    - "Paid electricity bill $120 last Friday"
                    """
    )
    public AITransactionResponse chatWithAI(
            @RequestParam(required = true) String prompt,
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        return aiChatService.chat(prompt, userDetail.getUser());
    }
}
