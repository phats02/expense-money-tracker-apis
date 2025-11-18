package com.ferb.expenseMoneyTracker.dto;

import lombok.Data;

@Data
public class UpdateWalletRequest {
    private String title;
    private String description;
}
