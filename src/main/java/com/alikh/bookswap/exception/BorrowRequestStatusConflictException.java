package com.alikh.bookswap.exception;

public class BorrowRequestStatusConflictException extends RuntimeException {
    public BorrowRequestStatusConflictException(String message) {
        super(message);
    }
}
