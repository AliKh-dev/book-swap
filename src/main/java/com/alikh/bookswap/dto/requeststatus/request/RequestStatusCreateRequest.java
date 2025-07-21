package com.alikh.bookswap.dto.requeststatus.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestStatusCreateRequest(
        @NotBlank
        @Size(max = 15)
        String code
) {
}
