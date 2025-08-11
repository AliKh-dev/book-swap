package com.alikh.bookswap.exception.parents;

import org.springframework.http.HttpStatus;

public abstract class AuthorizationException extends ApiException {

    protected AuthorizationException(String code, String message) {
        super(code, HttpStatus.FORBIDDEN, "Forbidden", message);
    }
}