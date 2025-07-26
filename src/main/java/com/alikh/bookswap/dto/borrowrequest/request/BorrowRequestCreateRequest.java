package com.alikh.bookswap.dto.borrowrequest.request;

import jakarta.validation.constraints.NotNull;

public record BorrowRequestCreateRequest(
        @NotNull
        Long listingId,
        Integer statusId
) {

    public BorrowRequestCreateRequest {
        if (statusId == null)
            statusId = 1;
    }
}
