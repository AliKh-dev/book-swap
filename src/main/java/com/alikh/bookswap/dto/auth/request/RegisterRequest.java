package com.alikh.bookswap.dto.auth.request;

import com.alikh.bookswap.dto.user.request.UserCreateRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
    public UserCreateRequest toUserCreateRequest() {
        return new UserCreateRequest(name, email, password);
    }
}
