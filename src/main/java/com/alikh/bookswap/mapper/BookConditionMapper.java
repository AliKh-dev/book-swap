package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.bookcondition.request.*;
import com.alikh.bookswap.dto.bookcondition.response.*;
import com.alikh.bookswap.entity.BookCondition;
import org.springframework.stereotype.Component;

@Component
public class BookConditionMapper {

    /* ---------- Entity -> DTO ---------- */

    public BookConditionSummaryResponse toSummary(BookCondition condition) {
        return new BookConditionSummaryResponse(
                condition.getId(),
                condition.getCode()
        );
    }

    public BookConditionDetailResponse toDetail(BookCondition condition) {
        return new BookConditionDetailResponse(
                condition.getId(),
                condition.getCode()
        );
    }

    /* ---------- Create / Update requests -> Entity ---------- */

    public BookCondition fromCreate(BookConditionCreateRequest request,
                                    Integer conditionId) {
        return BookCondition.builder()
                .id(conditionId)
                .code(request.code())
                .build();
    }

    public void applyUpdate(BookCondition condition,
                            BookConditionUpdateRequest request) {
        condition.setCode(request.code());
    }

    public void applyPatch(BookCondition condition,
                           BookConditionPatchRequest request) {
        if (request.code() != null) {
            condition.setCode(request.code());
        }
    }
}
