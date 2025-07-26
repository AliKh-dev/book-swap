package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.requeststatus.request.*;
import com.alikh.bookswap.dto.requeststatus.response.*;


import java.util.List;

public interface RequestStatusService {
    RequestStatusSummaryResponse create(RequestStatusCreateRequest dto);

    List<RequestStatusSummaryResponse> list();

    RequestStatusDetailResponse get(Integer id);

    RequestStatusSummaryResponse update(Integer id, RequestStatusUpdateRequest dto);

    RequestStatusSummaryResponse patch(Integer id, RequestStatusPatchRequest dto);

    void delete(Integer id);
}
