package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.AuthenticationException;

public class BadCredentialsException extends AuthenticationException {

    public BadCredentialsException() {
        super("BAD_CREDENTIALS", "Username or password is incorrect");
    }
}
