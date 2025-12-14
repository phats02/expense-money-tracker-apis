package com.ferb.expenseMoneyTracker.dto;

import com.ferb.expenseMoneyTracker.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response from AI chat with optional transaction creation via Function Calling
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AITransactionResponse {
    /**
     * Whether the operation was successful
     */
    private boolean success;

    /**
     * Response message from AI or confirmation of action
     */
    private String message;

    /**
     * Whether a function was called (vs regular text response)
     */
    private boolean functionCalled;

    /**
     * Name of the function that was called (if any)
     */
    private String functionName;

    /**
     * The created transaction (only present if functionCalled=true and success=true)
     */
    private Transaction transaction;

    /**
     * Parsed data from the AI (only present if functionCalled=true)
     */
    private ParsedTransactionData parsedData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParsedTransactionData {
        private String title;
        private String amount;
        private String date;
        private String categoryName;
        private String walletName;
        private String note;
    }
}
