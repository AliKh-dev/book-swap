package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.BusinessException;

public class InvalidListingException extends BusinessException {

    public InvalidListingException(String message) {
        super("INVALID_LISTING", message);
    }
}