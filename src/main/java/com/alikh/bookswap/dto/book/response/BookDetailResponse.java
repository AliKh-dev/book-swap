package com.alikh.bookswap.dto.book.response;

import java.time.LocalDateTime;

public record BookDetailResponse(
        Long id,
        String title,
        String author,
        String genre,
        String isbn,
        String description,
        String condition,
        String ownerName,
        LocalDateTime createdAt
) {
}
