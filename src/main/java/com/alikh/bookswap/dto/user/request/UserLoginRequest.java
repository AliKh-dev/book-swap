package com.alikh.bookswap.dto.user.request;

import com.alikh.bookswap.dto.auth.request.LoginRequest;

public record UserLoginRequest(
        String email,
        String password
) {
    public static UserLoginRequest from(LoginRequest request) {
        return new UserLoginRequest(request.email(), request.password());
    }
}
