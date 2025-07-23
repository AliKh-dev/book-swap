package com.alikh.bookswap.dto.user.response;

public record UserSummaryResponse(
        Long id,
        String email,
        String name,
        Integer roleId) {
}
