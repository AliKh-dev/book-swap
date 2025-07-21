package com.alikh.bookswap.dto.role.response;

import com.alikh.bookswap.dto.user.response.UserSummaryResponse;
import org.springframework.data.domain.Page;

public record RoleDetailResponse(
        Integer id,
        String code,
        Page<UserSummaryResponse> users
) {}
