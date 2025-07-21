package com.alikh.bookswap.dto.borrowrequest.response;

import java.time.LocalDateTime;

public record BorrowRequestSummaryResponse(
        Long id,
        Long listingId,
        Integer statusId,
        LocalDateTime requestedAt
) {
}
