package com.alikh.bookswap.dto.auth.response;

public record LoginResponse(
        Long userId,
        String email,
        String name,
        String accessToken,
        String refreshToken
) {
}
