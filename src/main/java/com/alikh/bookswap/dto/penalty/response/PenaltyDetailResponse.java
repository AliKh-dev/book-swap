package com.alikh.bookswap.dto.penalty.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PenaltyDetailResponse(
        Long id,
        Long requestId,
        BigDecimal amount,
        String reason,
        String penaltyType,
        String resolvedByName,
        LocalDateTime resolvedAt,
        Boolean isDeleted
) {
}
