package com.example.expenseMoneyTracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginRequest {
    @NotNull
    @Email(message = "Email is not valid")
    String email;

    @NotNull
    String password;
}
