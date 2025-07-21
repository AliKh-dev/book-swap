package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.bookcondition.request.*;
import com.alikh.bookswap.dto.bookcondition.response.*;

import java.util.List;

public interface BookConditionService {
    BookConditionSummaryResponse create(BookConditionCreateRequest dto);

    BookConditionDetailResponse get(Integer id);

    void update(Integer id, BookConditionUpdateRequest dto);

    void patch(Integer id, BookConditionPatchRequest dto);

    void delete(Integer id);

    List<BookConditionSummaryResponse> list();
}
