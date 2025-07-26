package com.alikh.bookswap.dto.user.request;

public record UserChangePasswordRequest(
        Long userId,
        String currentPassword,
        String newPassword
) {
}
