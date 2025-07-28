package com.alikh.bookswap.dto.borrowrequest.request;

import jakarta.validation.constraints.NotNull;

public record BorrowRequestCreateRequest(
        @NotNull
        Long listingId
) {
}
