package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.user.request.UserCreateRequest;
import com.alikh.bookswap.dto.user.request.UserPatchRequest;
import com.alikh.bookswap.dto.user.request.UserUpdateRequest;
import com.alikh.bookswap.dto.user.response.UserDetailResponse;
import com.alikh.bookswap.dto.user.response.UserSummaryResponse;

import java.util.List;

public interface UserService {
    UserSummaryResponse create(UserCreateRequest dto);

    UserDetailResponse get(Long id);

    List<UserSummaryResponse> list();

    void update(Long id, UserUpdateRequest dto);

    void patch(Long id, UserPatchRequest dto);

    void softDelete(Long id, String deleter);

    void hardDeleted(Long id);
}
