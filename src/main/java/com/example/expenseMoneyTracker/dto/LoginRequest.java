package com.example.expenseMoneyTracker.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginRequest {
    String email;
    String password;
}
