package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.penalty.request.*;
import com.alikh.bookswap.dto.penalty.response.*;

import java.util.List;

public interface PenaltyService {

    PenaltySummaryResponse create(PenaltyCreateRequest dto, Long reqId);

    List<PenaltySummaryResponse> list();

    PenaltyDetailResponse get(Long id);

    PenaltySummaryResponse update(Long id, PenaltyUpdateRequest dto, Long currentUserId);

    PenaltySummaryResponse patch(Long id, PenaltyPatchRequest dto, Long currentUserId);

    void softDelete(Long id, Long currentUserId);

    void hardDelete(Long id, Long currentUserId);
}
