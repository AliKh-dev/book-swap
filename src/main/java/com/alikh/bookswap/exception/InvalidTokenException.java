package com.alikh.bookswap.exception;

import io.jsonwebtoken.JwtException;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String invalidJwt, JwtException exception) {
    }
}
