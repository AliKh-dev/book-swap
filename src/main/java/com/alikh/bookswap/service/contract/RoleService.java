package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.role.request.*;
import com.alikh.bookswap.dto.role.response.*;
import com.alikh.bookswap.entity.Role;

import java.util.List;

public interface RoleService {

    RoleSummaryResponse create(RoleCreateRequest dto);

    List<RoleSummaryResponse> list();

    Role get(Integer id);

    RoleDetailResponse get(Integer id, int page);

    RoleSummaryResponse update(Integer id, RoleUpdateRequest dto);

    RoleSummaryResponse patch(Integer id, RolePatchRequest dto);

    void delete(Integer id);
}
