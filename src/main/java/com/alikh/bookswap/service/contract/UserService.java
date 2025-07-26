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

    void update(Long id, UserUpdateRequest dto);

    void patch(Long id, UserPatchRequest dto);

    void softDelete(Long id, String deleter);

    void hardDeleted(Long id);
}
