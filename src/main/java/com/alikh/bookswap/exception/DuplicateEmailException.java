package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.ConflictException;

public class DuplicateEmailException extends ConflictException {

    public DuplicateEmailException(String email) {
        super("DUPLICATE_EMAIL", "User with email '" + email + "' already exists.");
    }
}
