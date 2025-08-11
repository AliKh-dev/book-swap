package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.BusinessException;

public class InvalidStatusTransitionException extends BusinessException {

    public InvalidStatusTransitionException(int from, int to) {
        super("INVALID_STATUS_TRANSITION", "Cannot change status from " + from + " to " + to);
    }
}