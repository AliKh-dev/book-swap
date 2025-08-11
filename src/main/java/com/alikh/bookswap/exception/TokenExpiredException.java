package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {
    public TokenExpiredException() {
        super("TOKEN_EXPIRED", "Token has expired");
    }
}