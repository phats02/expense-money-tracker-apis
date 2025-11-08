package com.example.expenseMoneyTracker.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorType {
    RESOURCE_NOT_FOUND("Resource not found"),
    INVALID_REQUEST("Invalid request"),
    SERVER_ERROR("Server error"),
    INVALID_AUTH_TOKEN("Invalid auth token"),
    MALFORMED_TOKEN("Malformed token"),
    FORBIDDEN("Forbidden");

    private final String description;
    ErrorType(String description){
        this.description= description;
    }

    @JsonValue
    public String toString() {
        return this.description;
    }
}
