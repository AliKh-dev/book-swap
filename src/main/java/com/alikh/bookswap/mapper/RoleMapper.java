package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.role.request.*;
import com.alikh.bookswap.dto.role.response.*;
import com.alikh.bookswap.entity.Role;
import org.springframework.stereotype.Component;


@Component
public class RoleMapper {

    /* ---------- Entity -> DTO ---------- */

    public RoleSummaryResponse toSummary(Role role) {
        return new RoleSummaryResponse(role.getId(), role.getCode());
    }

    public RoleDetailResponse toDetail(Role role) {
        return new RoleDetailResponse(role.getId(), role.getCode());
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
