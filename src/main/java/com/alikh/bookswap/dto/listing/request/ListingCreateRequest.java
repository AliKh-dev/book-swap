package com.alikh.bookswap.dto.listing.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ListingCreateRequest(
        @NotNull
        Long bookId,

        @NotNull
        Integer typeId,

        @PositiveOrZero
        BigDecimal price,

        Integer rentalDays               // required only for LEND (typeId=1)
) {
}
