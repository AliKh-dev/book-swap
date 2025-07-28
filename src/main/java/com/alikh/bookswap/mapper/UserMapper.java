package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.user.request.*;
import com.alikh.bookswap.dto.user.response.*;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /* ---------- Entity → DTO ---------- */

    public UserSummaryResponse toSummary(AppUser user) {
        return new UserSummaryResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().getId()
        );
    }

    public UserDetailResponse toDetail(AppUser user) {
        return new UserDetailResponse(
                user.getEmail(),
                user.getName(),
                user.getRole().getId(),
                user.getCreatedAt()
        );
    }

    public UserForRoleResponse toForRole(AppUser user) {
        return new UserForRoleResponse(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    /* ---------- Create / Update → Entity ---------- */

    public AppUser fromCreate(UserCreateRequest request, Role role, String hashedPassword) {
        return AppUser.builder()
                .email(request.email())
                .password(hashedPassword)   // hash provided by service
                .name(request.name())
                .role(role)
                .build();
    }

    public void applyUpdate(AppUser user, UserUpdateRequest request) {
        user.setEmail(request.email());
        user.setName(request.name());
    }

    public void applyPatch(AppUser user, UserPatchRequest request) {
        if (request.email() != null) user.setEmail(request.email());
        if (request.name() != null) user.setName(request.name());
    }
}
