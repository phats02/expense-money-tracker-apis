package com.ferb.expenseMoneyTracker.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class LoginResponse  {
    @NonNull
    private String accessToken;
    private String refreshToken;
}
