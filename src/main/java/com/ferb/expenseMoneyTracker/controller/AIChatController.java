package com.ferb.expenseMoneyTracker.controller;

import com.ferb.expenseMoneyTracker.service.AIChatService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@SecurityRequirement(name = "bearerAuth")
@Tag(name="AI features ")
public class AIChatController {
    @Autowired
    AIChatService aiChatService;

    @PostMapping("/chat")
    public String chatWithAI(@RequestParam(required = true) String prompt){
        return aiChatService.chat(prompt);
    }
}
