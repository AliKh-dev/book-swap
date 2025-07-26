package com.alikh.bookswap.dto.auth.response;

import com.alikh.bookswap.dto.user.response.UserSummaryResponse;

public record RegisterResponse(
        Long id,
        String email,
        String name
) {
    public static RegisterResponse from(UserSummaryResponse user) {
        return new RegisterResponse(user.id(), user.email(), user.name());
    }
}