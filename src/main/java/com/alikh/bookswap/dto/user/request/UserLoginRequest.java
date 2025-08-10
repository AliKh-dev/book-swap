package com.alikh.bookswap.dto.user.request;

import com.alikh.bookswap.dto.auth.request.LoginRequest;

public record UserLoginRequest(
        String email,
        String password
) {
}
