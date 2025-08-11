package com.alikh.bookswap.exception.parents;

import org.springframework.http.HttpStatus;

public abstract class AuthenticationException extends ApiException {

    protected AuthenticationException(String code, String message) {
        super(code, HttpStatus.UNAUTHORIZED, "Authentication failed", message);
    }
}