package com.alikh.bookswap.dto.role.request;

import jakarta.validation.constraints.*;

public record RoleUpdateRequest(
        @NotBlank
        @Size(max = 20)
        String code
) {}
