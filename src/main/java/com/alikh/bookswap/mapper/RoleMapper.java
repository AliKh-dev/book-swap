package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.role.request.*;
import com.alikh.bookswap.dto.role.response.*;
import com.alikh.bookswap.dto.user.response.UserForRoleResponse;
import com.alikh.bookswap.dto.user.response.UserSummaryResponse;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final UserMapper userMapper;

    /* ---------- Entity -> DTO ---------- */

    public RoleSummaryResponse toSummary(Role role) {
        return new RoleSummaryResponse(role.getId(), role.getCode());
    }

    public RoleDetailResponse toDetail(Role role, List<AppUser> users) {
        return new RoleDetailResponse(
                role.getId(),
                role.getCode(),
                users.stream().map(userMapper::toForRole).toList());
    }

    /* ---------- Create / Update -> Entity ---------- */

    public Role fromCreate(RoleCreateRequest request, Integer roleId) {
        return Role.builder()
                .id(roleId)
                .code(request.code())
                .build();
    }

    public void applyUpdate(Role role, RoleUpdateRequest request) {
        role.setCode(request.code());
    }

    public void applyPatch(Role role, RolePatchRequest request) {
        if (request.code() != null) role.setCode(request.code());
    }
}
