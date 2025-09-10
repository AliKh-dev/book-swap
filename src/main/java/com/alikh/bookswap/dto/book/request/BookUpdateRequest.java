package com.alikh.bookswap.dto.book.request;

import jakarta.validation.constraints.*;

public record BookUpdateRequest(
        @NotBlank
        @Size(max = 255)
        String title,

        @NotBlank
        @Size(max = 255)
        String author,

        @NotBlank
        @Size(max = 100)
        String genre,

        @NotBlank
        @Size(max = 20)
        String isbn,

        @NotBlank
        @Size(max = 1000)
        String description,

        @NotNull
        Integer conditionId
) {
}
