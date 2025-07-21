package com.alikh.bookswap.dto.borrowrequest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BorrowRequestDetailResponse(
        Long id,
        Long listingId,
        String borrowerName,
        String statusCode,
        BigDecimal penaltyAmount,
        String penaltyReason,
        LocalDateTime requestedAt,
        LocalDateTime approvedAt,
        LocalDateTime rejectedAt,
        LocalDateTime returnedAt
) {
}
