package com.alikh.bookswap.service.contract;

import com.alikh.bookswap.dto.bookcondition.request.*;
import com.alikh.bookswap.dto.bookcondition.response.*;

import java.util.List;

public interface BookConditionService {
    BookConditionSummaryResponse create(BookConditionCreateRequest dto);

    List<BookConditionSummaryResponse> list();

    BookConditionDetailResponse get(Integer id);

    BookConditionSummaryResponse update(Integer id, BookConditionUpdateRequest dto);

    BookConditionSummaryResponse patch(Integer id, BookConditionPatchRequest dto);

    void delete(Integer id);
}
