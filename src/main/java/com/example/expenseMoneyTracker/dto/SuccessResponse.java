package com.example.expenseMoneyTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse<T> {
    private final boolean success= true;
    private T data;
}
