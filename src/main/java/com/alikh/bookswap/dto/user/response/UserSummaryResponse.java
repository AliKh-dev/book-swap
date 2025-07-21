package com.alikh.bookswap.dto.user.response;

public record UserSummaryResponse(
        String email,
        String name,
        Integer roleId) {}
