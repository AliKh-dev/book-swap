package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.penaltytype.request.*;
import com.alikh.bookswap.dto.penaltytype.response.*;

import java.util.List;

public interface PenaltyTypeService {
    PenaltyTypeSummaryResponse create(PenaltyTypeCreateRequest dto);

    PenaltyTypeDetailResponse get(Integer id);

    void update(Integer id, PenaltyTypeUpdateRequest dto);

    void patch(Integer id, PenaltyTypePatchRequest dto);

    void delete(Integer id);

    List<PenaltyTypeSummaryResponse> list();
}
