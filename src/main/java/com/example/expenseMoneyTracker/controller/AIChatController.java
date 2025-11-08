package com.example.expenseMoneyTracker.controller;

import com.example.expenseMoneyTracker.service.AIChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIChatController {
    @Autowired
    AIChatService aiChatService;

    @PostMapping("/chat")
    public String chatWithAI(@RequestParam(required = true) String prompt){
        return aiChatService.chat(prompt);
    }
}
