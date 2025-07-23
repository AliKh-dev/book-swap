package com.alikh.bookswap.dto.role.request;

import jakarta.validation.constraints.*;

public record RoleCreateRequest(
        @NotBlank
        @Size(max = 20)
        String code) {
}
