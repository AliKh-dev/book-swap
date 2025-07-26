package com.alikh.bookswap.dto.auth.response;

public record RefreshResponse(
        String email,
        String newAccessToken,
        String newRefreshToken
) {
}
