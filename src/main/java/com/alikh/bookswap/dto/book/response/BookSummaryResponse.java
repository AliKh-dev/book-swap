package com.alikh.bookswap.dto.book.response;

public record BookSummaryResponse(
        Long id,
        String title,
        String author,
        String genre,
        String isbn
) {
}
