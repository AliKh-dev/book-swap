package com.alikh.bookswap.dto.penalty.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PenaltyDetailResponse(
        Long id,
        Long requestId,
        Integer typeId,
        BigDecimal amount,
        String reason,
        Long resolvedById,
        LocalDateTime resolvedAt,
        Boolean isDeleted
) {
}
