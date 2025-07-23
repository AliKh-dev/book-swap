package com.alikh.bookswap.exception;

public class CodeAlreadyExists extends RuntimeException {

    public CodeAlreadyExists(String entity, String code) {
        super(entity + " with code '" + code + "' already exists.");
    }
}
