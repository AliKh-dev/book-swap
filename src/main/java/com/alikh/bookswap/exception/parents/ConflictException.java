package com.alikh.bookswap.exception.parents;

import org.springframework.http.HttpStatus;

public abstract class ConflictException extends ApiException {

    protected ConflictException(String code, String message) {
        super(code, HttpStatus.CONFLICT, "Conflict", message);
    }
}
