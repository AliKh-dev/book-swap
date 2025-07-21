package com.alikh.bookswap.dto.penalty.response;

import java.math.BigDecimal;

public record PenaltySummaryResponse(
        Long id,
        Long requestId,
        Integer typeId,
        BigDecimal amount,
        Boolean isDeleted
) {
}
