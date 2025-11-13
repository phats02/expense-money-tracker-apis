package com.ferb.expenseMoneyTracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SignupByPasswordRequest {
    @NotNull
    @Email(message = "Email is not valid")
    String email;

    @NotNull
    String password;
}
