package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.penalty.request.*;
import com.alikh.bookswap.dto.penalty.response.*;

import java.util.List;

public interface PenaltyService {

    PenaltySummaryResponse create(PenaltyCreateRequest dto);

    PenaltyDetailResponse get(Long id);

    List<PenaltySummaryResponse> list();

    void update(Long id, PenaltyUpdateRequest dto);

    void patch(Long id, PenaltyPatchRequest dto);

    void delete(Long id);
}
