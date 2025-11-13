package com.ferb.expenseMoneyTracker.exception;

public class FieldAlreadyExisted extends RuntimeException {
    public FieldAlreadyExisted(String field) {
        super(String.format("%s already existed",field));
    }
}
