package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.penaltytype.request.*;
import com.alikh.bookswap.dto.penaltytype.response.*;

import java.util.List;

public interface PenaltyTypeService {
    PenaltyTypeSummaryResponse create(PenaltyTypeCreateRequest dto);

    List<PenaltyTypeSummaryResponse> list();

    PenaltyTypeDetailResponse get(Integer id);

    PenaltyTypeSummaryResponse update(Integer id, PenaltyTypeUpdateRequest dto);

    PenaltyTypeSummaryResponse patch(Integer id, PenaltyTypePatchRequest dto);

    void delete(Integer id);
}
