package com.example.expenseMoneyTracker.dto;

import com.example.expenseMoneyTracker.enums.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ErrorResponse<T> {
    private ErrorType type;
    private T message;
    private final boolean success= false;
}
