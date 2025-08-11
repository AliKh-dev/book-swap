package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.AuthenticationException;

public class RefreshTokenMissingException extends AuthenticationException {

    public RefreshTokenMissingException() {
        super("REFRESH_TOKEN_MISSING", "Refresh token is missing");
    }
}
