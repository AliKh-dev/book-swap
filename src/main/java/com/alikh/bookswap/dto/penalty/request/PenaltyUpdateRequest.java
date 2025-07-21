package com.alikh.bookswap.dto.penalty.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PenaltyUpdateRequest(
        @NotNull
        Integer typeId,

        @PositiveOrZero
        BigDecimal amount,

        @Size(max = 500)
        String reason,

        Long resolvedById   // admin user ID
) {
}

