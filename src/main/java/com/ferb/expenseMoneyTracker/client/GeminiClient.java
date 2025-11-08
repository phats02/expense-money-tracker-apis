package com.ferb.expenseMoneyTracker.client;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Component;
import java.util.Objects;


@Component
public class GeminiClient {
    private final Client client;
    private final String modelName = "gemini-2.5-flash-lite";

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

    public void close() {
        client.close();
    }
}
