package com.alikh.bookswap.dto.book.cache;

import java.io.Serializable;
import java.time.LocalDateTime;

public record BookCacheView(
        Long id,
        String title,
        String author,
        String genre,
        String isbn,
        String description,
        String condition,
        String ownerName,
        LocalDateTime createdAt
) implements Serializable {
}
