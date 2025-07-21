package com.alikh.bookswap.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entityName, Object id) {
        super(entityName + " with id=" + id + " not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
