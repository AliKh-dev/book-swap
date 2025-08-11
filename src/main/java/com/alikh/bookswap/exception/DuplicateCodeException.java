package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.ConflictException;

public class DuplicateCodeException extends ConflictException {

    public DuplicateCodeException(String entity, String code) {
        super("DUPLICATE_CODE", entity + " with code '" + code + "' already in use.");
    }
}
