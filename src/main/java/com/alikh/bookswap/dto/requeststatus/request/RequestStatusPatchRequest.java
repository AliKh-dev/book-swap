package com.alikh.bookswap.dto.requeststatus.request;

public record RequestStatusPatchRequest(
        String code   // nullable for partial update
) {
}
