package com.alikh.bookswap.exception;

public class BadCredentialsException extends UnauthorizedException {

    public BadCredentialsException(String message) {
        super(message);
    }
}
