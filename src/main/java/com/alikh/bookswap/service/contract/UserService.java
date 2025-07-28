package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.user.request.*;
import com.alikh.bookswap.dto.user.response.UserDetailResponse;
import com.alikh.bookswap.dto.user.response.UserSummaryResponse;
import com.alikh.bookswap.entity.AppUser;

import java.util.List;

public interface UserService {
    UserSummaryResponse create(UserCreateRequest dto);

    AppUser authenticate(UserLoginRequest dto);

    void changePassword(UserChangePasswordRequest dto);

    AppUser getEntity(Long id);

    UserDetailResponse get(Long id);

    List<UserSummaryResponse> list();

    UserSummaryResponse update(Long id, UserUpdateRequest dto);

    UserSummaryResponse patch(Long id, UserPatchRequest dto);

    UserSummaryResponse changeRole(Long id, UserRoleChangeRequest dto);

    void softDelete(Long id, Long currentUserId);

    void hardDeleted(Long id, Long currentUserId);
}
