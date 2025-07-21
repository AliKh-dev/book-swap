package com.alikh.bookswap.dto.listing.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ListingDetailResponse(
        Long id,
        Long bookId,
        Integer typeId,
        BigDecimal price,
        Integer rentalDays,
        Boolean isActive,
        Long activeBookId,
        LocalDateTime createdAt
) {
}
