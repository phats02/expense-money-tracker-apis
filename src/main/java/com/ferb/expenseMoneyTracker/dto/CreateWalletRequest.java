package com.ferb.expenseMoneyTracker.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateWalletRequest {

    @NotNull
    private String title;
    private String description;
    @Digits(integer = 10, fraction = 3)
    private BigDecimal balance;
}
