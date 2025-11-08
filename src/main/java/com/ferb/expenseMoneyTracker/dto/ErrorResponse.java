package com.ferb.expenseMoneyTracker.dto;

import com.ferb.expenseMoneyTracker.enums.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse<T> {
    private ErrorType type;
    private T message;
    private final boolean success= false;
}
