package com.alikh.bookswap.dto.user.request;

import jakarta.validation.constraints.*;

public record UserCreateRequest(
        @NotBlank
        String name,

        @NotBlank @Email
        String email,

        @NotBlank
        String password
) {
}
