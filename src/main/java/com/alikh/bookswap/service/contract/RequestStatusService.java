package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.requeststatus.request.*;
import com.alikh.bookswap.dto.requeststatus.response.*;


import java.util.List;

public interface RequestStatusService {
    RequestStatusSummaryResponse create(RequestStatusCreateRequest dto);

    RequestStatusDetailResponse get(Integer id);

    void update(Integer id, RequestStatusUpdateRequest dto);

    void patch(Integer id, RequestStatusPatchRequest dto);

    void delete(Integer id);

    List<RequestStatusSummaryResponse> list();
}
