package com.alikh.bookswap.dto.auth;

public record ChangePasswordRequest (
        String oldPassword,
        String newPassword
) {
}
