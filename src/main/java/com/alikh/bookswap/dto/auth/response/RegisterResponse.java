package com.alikh.bookswap.dto.auth.response;

import com.alikh.bookswap.dto.user.response.UserSummaryResponse;
import com.alikh.bookswap.entity.AppUser;

public record RegisterResponse(
        Long id,
        String email,
        String name
) {
    public static RegisterResponse from(AppUser user) {
        return new RegisterResponse(user.getId(), user.getEmail(), user.getName());
    }
}