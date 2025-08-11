package com.alikh.bookswap.exception;

import com.alikh.bookswap.exception.parents.ConflictException;

public class DuplicateApprovedBorrowRequestException extends ConflictException {

    public DuplicateApprovedBorrowRequestException(Long listingId) {
        super("DUPLICATE_APPROVED_BORROW_REQUEST", "Listing " + listingId + " already has an APPROVED borrow request");
    }
}