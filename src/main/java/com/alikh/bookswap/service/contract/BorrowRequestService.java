package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.borrowrequest.request.*;
import com.alikh.bookswap.dto.borrowrequest.response.*;

import java.util.List;

public interface BorrowRequestService {

    BorrowRequestSummaryResponse create(BorrowRequestCreateRequest dto, Long borrowerId);

    List<BorrowRequestSummaryResponse> list();

    List<BorrowRequestSummaryResponse> list(Long borrowerId);

    List<BorrowRequestSummaryResponse> listRelatedToOwner(Long bookOwnerId);

    BorrowRequestDetailResponse get(Long id);

    BorrowRequestSummaryResponse update(Long id, Long currentUserId, BorrowRequestUpdateRequest dto);

    BorrowRequestSummaryResponse patch(Long id, Long currentUserId, BorrowRequestPatchRequest dto);

    void softDelete(Long id, Long currentUserId);

    void hardDelete(Long id, Long currentUserId);
}
