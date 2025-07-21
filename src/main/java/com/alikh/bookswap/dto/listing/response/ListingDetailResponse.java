package com.alikh.bookswap.dto.listing.response;

import com.alikh.bookswap.dto.book.response.BookSummaryResponse;

import java.math.BigDecimal;

public record ListingDetailResponse(
        Long id,
        BigDecimal price,
        Integer rentalDays,
        Boolean isActive,
        Long activeBookId,
        String listingType,
        BookSummaryResponse book
) {
}
