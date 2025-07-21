package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.penaltytype.request.*;
import com.alikh.bookswap.dto.penaltytype.response.*;
import com.alikh.bookswap.entity.PenaltyType;
import org.springframework.stereotype.Component;

@Component
public class PenaltyTypeMapper {

    /* ---------- Entity -> DTO ---------- */

    public PenaltyTypeSummaryResponse toSummary(PenaltyType type) {
        return new PenaltyTypeSummaryResponse(
                type.getId(),
                type.getCode()
        );
    }

    public PenaltyTypeDetailResponse toDetail(PenaltyType type) {
        return new PenaltyTypeDetailResponse(
                type.getId(),
                type.getCode()
        );
    }

    /* ---------- Create / Update requests -> Entity ---------- */

    public PenaltyType fromCreate(PenaltyTypeCreateRequest request,
                                  Integer typeId) {

        return PenaltyType.builder()
                .id(typeId)
                .code(request.code())
                .build();
    }

    public void applyUpdate(PenaltyType type,
                            PenaltyTypeUpdateRequest request) {

        type.setCode(request.code());
    }

    public void applyPatch(PenaltyType type,
                           PenaltyTypePatchRequest request) {

        if (request.code() != null) {
            type.setCode(request.code());
        }
    }
}
