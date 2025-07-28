package com.alikh.bookswap.exception;

public class UnauthorizedStatusChangeException extends RuntimeException {
    public UnauthorizedStatusChangeException(String message) {
        super(message);
    }
}
