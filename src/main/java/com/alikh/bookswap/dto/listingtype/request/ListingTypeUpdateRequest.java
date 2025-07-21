package com.alikh.bookswap.dto.listingtype.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ListingTypeUpdateRequest(
        @NotBlank
        @Size(max = 10)
        String code
) {
}