package com.alikh.bookswap.exception;

public class CodeAlreadyExistsException extends RuntimeException {

    public CodeAlreadyExistsException(String entity, String code) {
        super(entity + " with code '" + code + "' already in use.");
    }
}
