package com.alikh.bookswap.dto.role.request;

public record RolePatchRequest(
        // nullable -> only update when provided
        String code) {}

