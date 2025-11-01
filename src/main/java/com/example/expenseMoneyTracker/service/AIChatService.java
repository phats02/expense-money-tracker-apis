package com.example.expenseMoneyTracker.service;

import com.example.expenseMoneyTracker.client.GeminiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIChatService {
    @Autowired
    private final GeminiClient geminiClient;

    public AIChatService() {
        this.geminiClient = new GeminiClient();
    }

    public String chat(String prompt) {
        return geminiClient.generateContent(prompt).text();
    }
}
