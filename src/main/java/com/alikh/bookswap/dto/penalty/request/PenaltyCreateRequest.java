package com.alikh.bookswap.dto.penalty.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PenaltyCreateRequest(
        @NotNull
        Long requestId,      // FK to BorrowRequest

        @NotNull
        Integer typeId,      // FK to PenaltyType

        @PositiveOrZero
        BigDecimal amount,

        @Size(max = 500)
        String reason
) {
}

