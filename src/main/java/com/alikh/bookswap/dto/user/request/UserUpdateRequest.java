package com.alikh.bookswap.dto.user.request;

import jakarta.validation.constraints.*;

public record UserUpdateRequest(
        @NotBlank @Email
        String email,

        @NotBlank
        String name,

        @NotNull
        Integer roleId) {
}
