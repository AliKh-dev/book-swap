package com.alikh.bookswap.dto.role.response;

import com.alikh.bookswap.dto.user.response.UserForRoleResponse;

import java.util.List;

public record RoleDetailResponse(
        Integer id,
        String code,
        List<UserForRoleResponse> users
) {
}
