package com.alikh.bookswap.dto.penaltytype.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PenaltyTypeUpdateRequest(
        @NotBlank
        @Size(max = 20)
        String code
) {
}
