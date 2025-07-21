package com.alikh.bookswap.dto.bookcondition.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookConditionCreateRequest(
        @NotBlank
        @Size(max = 20)
        String code
) {
}
