package com.ferb.expenseMoneyTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class RefreshTokenRequest {
    @NotNull
    String accessToken;

    @NotNull
    String refreshToken;
}
