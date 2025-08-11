package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.AuthorizationException;

public class AccessDeniedException extends AuthorizationException {

    public AccessDeniedException(String message) {
        super("ACCESS_DENIED", message);
    }
}
