package com.ferb.expenseMoneyTracker.exception;

import com.ferb.expenseMoneyTracker.dto.ErrorResponse;
import com.ferb.expenseMoneyTracker.enums.ErrorType;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse<String> handleJwtException(JwtException ex) {
        return new ErrorResponse<>(ErrorType.INVALID_AUTH_TOKEN, ex.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse<String> handleMalformedJwtException(MalformedJwtException ex) {
        return new ErrorResponse<>(ErrorType.MALFORMED_TOKEN, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ErrorResponse<>(ErrorType.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<String> handleMissingRequestHeaderException(MissingRequestHeaderException ex){
        return new ErrorResponse<>(ErrorType.INVALID_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ErrorResponse<>(ErrorType.INVALID_REQUEST, "Email or password is incorrect");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<Map<String, String>> handleBindException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ErrorResponse<>(ErrorType.INVALID_REQUEST, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<String> handleHttpMessageNotReadableException () {
        return new ErrorResponse<>(ErrorType.INVALID_REQUEST, "Bad request");
    }

    @ExceptionHandler(FieldAlreadyExisted.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<String> handleFieldAlreadyExisted(FieldAlreadyExisted ex) {
        return  new ErrorResponse<>(ErrorType.FIELD_ALREADY_EXISTED, ex.getMessage());
    }

    @ExceptionHandler(NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse<String> handleNotFound(NotFound ex) {
        return new ErrorResponse<>(ErrorType.RESOURCE_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse<String> handleRestExceptions(Exception ex) {
        log.error("Internal server error: "+ ex);
        return new ErrorResponse<>(ErrorType.SERVER_ERROR, ex.getMessage());
    }

}
