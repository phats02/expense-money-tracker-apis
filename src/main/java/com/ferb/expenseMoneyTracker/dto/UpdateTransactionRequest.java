package com.ferb.expenseMoneyTracker.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateTransactionRequest {
    private UUID categoryId;
    private String note;
    private String amount;
    private UUID walletId;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
