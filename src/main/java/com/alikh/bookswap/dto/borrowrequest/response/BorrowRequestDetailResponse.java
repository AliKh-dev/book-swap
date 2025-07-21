package com.alikh.bookswap.dto.borrowrequest.response;

import java.time.LocalDateTime;

public record BorrowRequestDetailResponse(
        Long id,
        Long listingId,
        Long borrowerId,
        Integer statusId,
        LocalDateTime requestedAt,
        LocalDateTime approvedAt,
        LocalDateTime rejectedAt,
        LocalDateTime returnedAt
) {
}
