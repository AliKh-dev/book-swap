package com.alikh.bookswap.exception;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(int from, int to) {
        super("Cannot change status from " + from + " to " + to);
    }
}