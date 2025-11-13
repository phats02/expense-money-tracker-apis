package com.ferb.expenseMoneyTracker.dto;

import com.ferb.expenseMoneyTracker.annotations.ToLowerCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SignupByPasswordRequest {
    @NotNull
    @Email(message = "Email is not valid")
    @ToLowerCase
    String email;

    @NotNull
    String password;
}
