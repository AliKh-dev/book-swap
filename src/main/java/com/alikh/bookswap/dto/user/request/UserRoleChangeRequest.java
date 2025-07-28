package com.alikh.bookswap.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record UserRoleChangeRequest(
        @NotBlank
        Integer roleId
) {
}
