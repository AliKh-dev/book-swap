package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.listingtype.request.*;
import com.alikh.bookswap.dto.listingtype.response.*;
import com.alikh.bookswap.entity.ListingType;
import org.springframework.stereotype.Component;

@Component
public class ListingTypeMapper {

    /* ---------- Entity -> DTO ---------- */

    public ListingTypeSummaryResponse toSummary(ListingType type) {
        return new ListingTypeSummaryResponse(
                type.getId(),
                type.getCode()
        );
    }

    public ListingTypeDetailResponse toDetail(ListingType type) {
        return new ListingTypeDetailResponse(
                type.getId(),
                type.getCode()
        );
    }

    /* ---------- Create / Update requests -> Entity ---------- */

    public ListingType fromCreate(ListingTypeCreateRequest request,
                                  Integer typeId) {
        return ListingType.builder()
                .id(typeId)
                .code(request.code())
                .build();
    }

    public void applyUpdate(ListingType type,
                            ListingTypeUpdateRequest request) {
        type.setCode(request.code());
    }

    public void applyPatch(ListingType type,
                           ListingTypePatchRequest request) {
        if (request.code() != null) {
            type.setCode(request.code());
        }
    }
}
