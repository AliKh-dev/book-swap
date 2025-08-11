package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException() {
        super("INVALID_TOKEN", "Token is invalid");
    }
}
