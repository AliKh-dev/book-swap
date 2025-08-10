package com.alikh.bookswap.dto.auth.request;

import com.alikh.bookswap.dto.user.request.UserLoginRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank @Email
        String email,

        @NotBlank
        String password
) {
    public UserLoginRequest toUserLoginRequest() {
        return new UserLoginRequest(email, password);
    }
}
