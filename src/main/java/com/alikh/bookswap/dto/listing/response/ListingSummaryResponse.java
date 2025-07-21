package com.alikh.bookswap.dto.listing.response;

import java.math.BigDecimal;

public record ListingSummaryResponse(
        Long id,
        Long bookId,
        Integer typeId,
        BigDecimal price,
        Boolean isActive
) {
}
