package com.alikh.bookswap.exception;

public class DuplicatePenaltyException extends RuntimeException {

    public DuplicatePenaltyException(Long requestId) {
        super("Borrow request " + requestId + " already has a penalty");
    }
}

