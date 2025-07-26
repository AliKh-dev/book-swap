package com.alikh.bookswap.dto.book.request;

import jakarta.validation.constraints.*;

public record BookCreateRequest(
        @NotBlank
        @Size(max = 255)
        String title,

        @NotBlank
        @Size(max = 255)
        String author,

        @Size(max = 100)
        String genre,

        @Size(max = 13)
        String isbn,

        @Size(max = 1000)
        String description,

        @NotNull
        Integer conditionId
) {
}
