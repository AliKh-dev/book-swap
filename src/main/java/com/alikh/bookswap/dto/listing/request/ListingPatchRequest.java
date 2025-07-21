package com.alikh.bookswap.dto.listing.request;

import java.math.BigDecimal;

public record ListingPatchRequest(
        Integer typeId,
        BigDecimal price,
        Integer rentalDays,
        Boolean isActive
) {
}
