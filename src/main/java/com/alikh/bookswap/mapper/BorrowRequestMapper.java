package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.borrowrequest.request.*;
import com.alikh.bookswap.dto.borrowrequest.response.*;
import com.alikh.bookswap.entity.BorrowRequest;
import com.alikh.bookswap.entity.Listing;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.RequestStatus;
import org.springframework.stereotype.Component;

@Component
public class BorrowRequestMapper {

    /* ---------- Entity -> DTO ---------- */

    public BorrowRequestSummaryResponse toSummary(BorrowRequest req) {
        return new BorrowRequestSummaryResponse(
                req.getId(),
                req.getListing()  != null ? req.getListing().getId()  : null,
                req.getStatus()   != null ? req.getStatus().getId()   : null,
                req.getRequestedAt()
        );
    }

    public BorrowRequestDetailResponse toDetail(BorrowRequest req) {
        return new BorrowRequestDetailResponse(
                req.getId(),
                req.getListing() != null ? req.getListing().getId() : null,
                req.getBorrower().getName(),
                req.getStatus() != null ? req.getStatus().getCode() : null,
                req.getPenalty() != null ? req.getPenalty().getAmount() : null,
                req.getPenalty() != null ? req.getPenalty().getReason() : null,
                req.getRequestedAt(),
                req.getApprovedAt(),
                req.getRejectedAt(),
                req.getReturnedAt()
        );
    }

    /* ---------- Create / Update requests -> Entity ---------- */

    public BorrowRequest fromCreate(BorrowRequestCreateRequest request,
                                    Listing listing,
                                    AppUser borrower,
                                    RequestStatus initStatus) {

        return BorrowRequest.builder()
                .listing(listing)
                .borrower(borrower)
                .status(initStatus)
                .requestedAt(java.time.LocalDateTime.now())
                .build();
    }

    public void applyUpdate(BorrowRequest req,
                            BorrowRequestUpdateRequest request,
                            RequestStatus status) {
        req.setStatus(status);
        updateTimestamps(req, status);
    }

    public void applyPatch(BorrowRequest req,
                           BorrowRequestPatchRequest request,
                           RequestStatus status) {
        if (request.statusId() != null) {
            req.setStatus(status);
            updateTimestamps(req, status);
        }
    }

    /* ---------- helper ---------- */

    private void updateTimestamps(BorrowRequest req, RequestStatus status) {
        switch (status.getCode()) {
            case "APPROVED" -> req.setApprovedAt(java.time.LocalDateTime.now());
            case "REJECTED" -> req.setRejectedAt(java.time.LocalDateTime.now());
            case "RETURNED" -> req.setReturnedAt(java.time.LocalDateTime.now());
            default -> { }
        }
    }
}
