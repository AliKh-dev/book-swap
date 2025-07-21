package com.alikh.bookswap.dto.penalty.request;

import java.math.BigDecimal;

public record PenaltyPatchRequest(
        Integer typeId,
        BigDecimal amount,
        String reason,
        Long resolvedById
) {
}
