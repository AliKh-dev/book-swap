package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.ConflictException;

public class DuplicateActiveListingException extends ConflictException {

    public DuplicateActiveListingException(Long bookId) {
        super("DUPLICATE_ACTIVE_LISTING", "Book " + bookId + " already has an active listing");
    }
}
