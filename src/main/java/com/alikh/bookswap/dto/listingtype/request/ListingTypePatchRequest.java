package com.alikh.bookswap.dto.listingtype.request;

public record ListingTypePatchRequest(
        // nullable for partial update
        String code
) {
}