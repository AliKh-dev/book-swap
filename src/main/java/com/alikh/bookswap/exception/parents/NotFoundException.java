package com.alikh.bookswap.exception.parents;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super("NOT_FOUND", HttpStatus.NOT_FOUND, "Resource not found", message);
    }
}
