package com.ferb.expenseMoneyTracker.exception;

public class NotFound extends RuntimeException {
    public NotFound(String name) {
        super(name + " not found!");
    }
}
