package com.ferb.expenseMoneyTracker.dto;

import com.ferb.expenseMoneyTracker.annotations.ValidEnum;
import com.ferb.expenseMoneyTracker.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateTransactionRequest {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String note;

    @Positive
    private BigDecimal amount;

    @NotNull
    private UUID walletId;

    @NotNull
    private UUID categoryId;

    @NotNull
    private String title;

    @NotNull
    @ValidEnum(enumClass = TransactionType.class)
    private TransactionType type;
}
