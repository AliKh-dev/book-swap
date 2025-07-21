package com.alikh.bookswap.dto.user.request;

import jakarta.validation.constraints.*;

public record UserCreateRequest(
        @NotBlank @Email
        String email,

        @NotBlank
        String password,

        @NotBlank
        String name,

        @NotNull
        Integer roleId) {}
