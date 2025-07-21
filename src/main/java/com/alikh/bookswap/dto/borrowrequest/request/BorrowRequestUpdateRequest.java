package com.alikh.bookswap.dto.borrowrequest.request;

import jakarta.validation.constraints.NotNull;

public record BorrowRequestUpdateRequest(
        @NotNull
        Integer statusId   // APPROVED / REJECTED / RETURNED
) {
}
