package com.alikh.bookswap.exception;

/**
 * Thrown when a user tries to create a second active listing
 * for the same book (business rule, unique constraint UQ_ACTIVE_LISTING).
 */
public class DuplicateActiveListingException extends RuntimeException {

    public DuplicateActiveListingException(Long bookId) {
        super("Book " + bookId + " already has an active listing");
    }
}
