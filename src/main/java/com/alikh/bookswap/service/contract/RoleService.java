package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.role.request.*;
import com.alikh.bookswap.dto.role.response.*;

import java.util.List;

public interface RoleService {

    RoleSummaryResponse create(RoleCreateRequest dto);

    RoleDetailResponse get(Integer id, int page);

    List<RoleSummaryResponse> list();

    void update(Integer id, RoleUpdateRequest dto);

    void patch(Integer id, RolePatchRequest dto);

    void delete(Integer id);
}
