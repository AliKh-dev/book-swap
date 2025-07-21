package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.requeststatus.request.*;
import com.alikh.bookswap.dto.requeststatus.response.*;
import com.alikh.bookswap.entity.RequestStatus;
import org.springframework.stereotype.Component;

@Component
public class RequestStatusMapper {

    /* ---------- Entity -> DTO ---------- */

    public RequestStatusSummaryResponse toSummary(RequestStatus status) {
        return new RequestStatusSummaryResponse(
                status.getId(),
                status.getCode()
        );
    }

    public RequestStatusDetailResponse toDetail(RequestStatus status) {
        return new RequestStatusDetailResponse(
                status.getId(),
                status.getCode()
        );
    }

    /* ---------- Create / Update requests -> Entity ---------- */

    public RequestStatus fromCreate(RequestStatusCreateRequest request,
                                    Integer statusId) {

        return RequestStatus.builder()
                .id(statusId)
                .code(request.code())
                .build();
    }

    public void applyUpdate(RequestStatus status,
                            RequestStatusUpdateRequest request) {

        status.setCode(request.code());
    }

    public void applyPatch(RequestStatus status,
                           RequestStatusPatchRequest request) {

        if (request.code() != null) {
            status.setCode(request.code());
        }
    }
}
