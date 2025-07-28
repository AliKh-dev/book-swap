package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.error.ApiError;
import com.alikh.bookswap.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError> handleTokenExpired(TokenExpiredException ex) {
        var body = new ApiError(
                "TOKEN_EXPIRED",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex) {
        ApiError body = new ApiError(
                "NOT_FOUND",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler({CodeAlreadyExistsException.class, EmailAlreadyExistsException.class})
    public ResponseEntity<ApiError> handleConflictExceptions(RuntimeException ex) {
        String code = ex instanceof CodeAlreadyExistsException
                ? "CODE_ALREADY_EXISTS"
                : "EMAIL_ALREADY_EXISTS";
        ApiError body = new ApiError(
                code,
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(BorrowRequestStatusConflictException.class)
    public ResponseEntity<ApiError> handleBorrowRequestStatusConflict(BorrowRequestStatusConflictException ex) {
        ApiError body = new ApiError(
                "BORROW_REQUEST_STATUS_CONFLICT",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler({AccessDeniedException.class, UnauthorizedException.class, UnauthorizedStatusChangeException.class})
    public ResponseEntity<ApiError> handleForbiddenExceptions(RuntimeException ex) {
        String code;
        if (ex instanceof UnauthorizedStatusChangeException) {
            code = "UNAUTHORIZED_STATUS_CHANGE";
        } else {
            code = "ACCESS_DENIED";
        }
        ApiError body = new ApiError(
                code,
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException ex) {
        ApiError body = new ApiError(
                "INVALID_TOKEN",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        var body = new ApiError(
                "VALIDATION_FAILED",
                "One or more fields are invalid",
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
