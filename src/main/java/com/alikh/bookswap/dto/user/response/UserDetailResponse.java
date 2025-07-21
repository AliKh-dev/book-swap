package com.alikh.bookswap.dto.user.response;

import java.time.LocalDateTime;

// TODO: Add some detail fields
public record UserDetailResponse(
        String email,
        String name,
        Integer roleId,
        LocalDateTime createdAt) {
}
