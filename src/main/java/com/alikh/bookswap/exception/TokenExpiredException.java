package com.alikh.bookswap.exception;

/**
 * Thrown when a JWT is syntactically valid but its exp claim is in the past.
 */
public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super("JWT has expired");
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}