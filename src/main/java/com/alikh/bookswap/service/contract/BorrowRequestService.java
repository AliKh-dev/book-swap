package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.borrowrequest.request.*;
import com.alikh.bookswap.dto.borrowrequest.response.*;

import java.util.List;

public interface BorrowRequestService {

    BorrowRequestSummaryResponse create(BorrowRequestCreateRequest dto);

    BorrowRequestDetailResponse get(Long id);

    List<BorrowRequestSummaryResponse> list();

    void update(Long id, BorrowRequestUpdateRequest dto);

    void patch(Long id, BorrowRequestPatchRequest dto);

    void delete(Long id);
}
