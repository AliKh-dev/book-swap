package com.alikh.bookswap.dto.listing.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ListingUpdateRequest(
        @NotNull
        Integer typeId,

        @PositiveOrZero
        BigDecimal price,

        Integer rentalDays,

        @NotNull
        Boolean isActive
) {
}
