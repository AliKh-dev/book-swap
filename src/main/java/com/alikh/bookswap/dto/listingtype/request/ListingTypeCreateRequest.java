package com.alikh.bookswap.dto.listingtype.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ListingTypeCreateRequest(
        @NotBlank
        @Size(max = 10)
        String code
) {
}