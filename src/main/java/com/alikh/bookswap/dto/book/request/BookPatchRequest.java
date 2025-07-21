package com.alikh.bookswap.dto.book.request;

public record BookPatchRequest(
        String title,
        String author,
        String genre,
        String isbn,
        String description,
        Integer conditionId
) {
}
