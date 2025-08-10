package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.user.request.*;
import com.alikh.bookswap.dto.user.response.UserDetailResponse;
import com.alikh.bookswap.dto.user.response.UserSummaryResponse;
import com.alikh.bookswap.entity.AppUser;

import java.util.List;

public interface UserService {
    AppUser create(UserCreateRequest dto);

    AppUser fetch(Long id);

    AppUser fetch(String email);

    AppUser authenticate(UserLoginRequest dto);

    void changePassword(UserChangePasswordRequest dto);

    UserDetailResponse get(Long id);

    List<UserSummaryResponse> list();

    UserSummaryResponse update(Long id, UserUpdateRequest dto);

    UserSummaryResponse patch(Long id, UserPatchRequest dto);

    UserSummaryResponse changeRole(Long id, UserRoleChangeRequest dto);

    void softDelete(Long id, Long currentUserId);

    void hardDeleted(Long id, Long currentUserId);
}
