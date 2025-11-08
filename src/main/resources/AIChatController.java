package com.example.expenseMoneyTracker.controller;

import com.example.expenseMoneyTracker.service.AIChatService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AIChatController {
    @Autowired
    private AIChatService aiChatService;

    @PostMapping("/ai/chat")
    public String chatWithAI (@RequestParam(required = true) String prompt) {
        return aiChatService.chat(prompt);
    }
}
