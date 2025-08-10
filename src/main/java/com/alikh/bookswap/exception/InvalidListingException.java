package com.alikh.bookswap.exception;

/**
 * Thrown when a Listing payload violates a business rule that is
 * more specific than generic Bean-Validation errors
 * (e.g. LEND without rentalDays, negative price, etc.).
 * Mapped to 422 Unprocessable Entity in GlobalExceptionHandler.
 */
public class InvalidListingException extends RuntimeException {

    public InvalidListingException(String message) {
        super(message);
    }
}