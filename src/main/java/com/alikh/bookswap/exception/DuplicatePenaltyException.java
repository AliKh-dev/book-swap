package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.ConflictException;

public class DuplicatePenaltyException extends ConflictException {

    public DuplicatePenaltyException(Long requestId) {
        super("DUPLICATE_PENALTY", "Borrow request " + requestId + " already has a penalty");
    }
}

