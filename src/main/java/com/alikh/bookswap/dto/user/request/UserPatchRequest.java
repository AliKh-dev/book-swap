package com.alikh.bookswap.dto.user.request;

public record UserPatchRequest(
        String email,
        String password,
        String name,
        Integer roleId
) {}