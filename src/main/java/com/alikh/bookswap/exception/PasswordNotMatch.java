package com.alikh.bookswap.exception;

public class PasswordNotMatch extends RuntimeException {

    public PasswordNotMatch() {
        super("Entered password doesn't match");
    }
}
