package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.penalty.request.*;
import com.alikh.bookswap.dto.penalty.response.*;
import com.alikh.bookswap.entity.BorrowRequest;
import com.alikh.bookswap.entity.Penalty;
import com.alikh.bookswap.entity.PenaltyType;
import com.alikh.bookswap.entity.AppUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PenaltyMapper {

    /* ---------- Entity -> DTO ---------- */

    public PenaltySummaryResponse toSummary(Penalty penalty) {
        return new PenaltySummaryResponse(
                penalty.getId(),
                penalty.getRequest() != null ? penalty.getRequest().getId() : null,
                penalty.getType() != null ? penalty.getType().getId() : null,
                penalty.getAmount(),
                penalty.getIsDeleted()
        );
    }

    public PenaltyDetailResponse toDetail(Penalty penalty) {
        return new PenaltyDetailResponse(
                penalty.getId(),
                penalty.getRequest() != null ? penalty.getRequest().getId() : null,
                penalty.getType() != null ? penalty.getType().getId() : null,
                penalty.getAmount(),
                penalty.getReason(),
                penalty.getResolvedBy() != null ? penalty.getResolvedBy().getId() : null,
                penalty.getResolvedAt(),
                penalty.getIsDeleted()
        );
    }

    /* ---------- Create / Update requests -> Entity ---------- */

    public Penalty fromCreate(PenaltyCreateRequest request,
                              Long penaltyId,
                              BorrowRequest borrowReq,
                              PenaltyType type) {

        return Penalty.builder()
                .id(penaltyId)
                .request(borrowReq)
                .type(type)
                .amount(request.amount())
                .reason(request.reason())
                .resolvedBy(null)          // not resolved at creation time
                .resolvedAt(null)
                .build();
    }

    public void applyUpdate(Penalty penalty,
                            PenaltyUpdateRequest request,
                            PenaltyType type,
                            AppUser resolvedBy) {

        penalty.setType(type);
        penalty.setAmount(request.amount());
        penalty.setReason(request.reason());
        penalty.setResolvedBy(resolvedBy);
        penalty.setResolvedAt(resolvedBy != null ? LocalDateTime.now() : null);
    }

    public void applyPatch(Penalty penalty,
                           PenaltyPatchRequest request,
                           PenaltyType type,
                           AppUser resolvedBy) {

        if (request.typeId() != null) penalty.setType(type);
        if (request.amount() != null) penalty.setAmount(request.amount());
        if (request.reason() != null) penalty.setReason(request.reason());
        if (request.resolvedById() != null) {
            penalty.setResolvedBy(resolvedBy);
            penalty.setResolvedAt(LocalDateTime.now());
        }
    }
}
