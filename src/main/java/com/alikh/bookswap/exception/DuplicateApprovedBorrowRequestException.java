package com.alikh.bookswap.exception;

public class DuplicateApprovedBorrowRequestException extends RuntimeException {

    public DuplicateApprovedBorrowRequestException(Long listingId) {
        super("Listing " + listingId + " already has an APPROVED borrow request");
    }
}