package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.borrowrequest.request.*;
import com.alikh.bookswap.dto.borrowrequest.response.*;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.BorrowRequest;
import com.alikh.bookswap.entity.Listing;
import com.alikh.bookswap.entity.RequestStatus;
import com.alikh.bookswap.exception.*;
import com.alikh.bookswap.exception.parents.NotFoundException;
import com.alikh.bookswap.mapper.BorrowRequestMapper;
import com.alikh.bookswap.repository.BorrowRequestRepository;
import com.alikh.bookswap.repository.ListingRepository;
import com.alikh.bookswap.repository.RequestStatusRepository;
import com.alikh.bookswap.repository.UserRepository;
import com.alikh.bookswap.service.contract.BorrowRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowRequestServiceImpl implements BorrowRequestService {

    private static final int STATUS_PENDING = 1;
    private static final int STATUS_APPROVED = 2;
    private static final int STATUS_REJECTED = 3;
    private static final int STATUS_RETURNED = 4;

    private final BorrowRequestRepository repo;
    private final ListingRepository listingRepo;
    private final RequestStatusRepository statusRepo;
    private final UserRepository userRepo;
    private final BorrowRequestMapper mapper;

    @Override
    public BorrowRequestSummaryResponse create(
            Long listingId,
            Long borrowerId
    ) {
        var listing = fetchActiveListingOrThrow(listingId);
        if (listing.getBook().getOwner().getId().equals(borrowerId))
            // TODO: I should change the exception type & message
            throw new AccessDeniedException("This book belong to you.");

        var borrower = fetchActiveUserOrThrow(borrowerId);
        var status = fetchStatusOrThrow(STATUS_PENDING);

        var entity = mapper.fromCreate(listing, borrower, status);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRequestSummaryResponse> listMine(Long currentUserId) {
        return repo.findByBorrowerIdAndIsDeletedFalse(currentUserId).stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRequestSummaryResponse> listByOwner(Long bookOwnerId) {
        return repo.findByListingBookOwnerIdAndIsDeletedFalse(bookOwnerId).stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRequestSummaryResponse> list() {
        return repo.findByIsDeletedFalse().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BorrowRequestDetailResponse get(Long id) {
        return mapper.toDetail(fetchBorrowRequestOrThrow(id));
    }

    @Override
    public BorrowRequestSummaryResponse update(
            Long id,
            Long currentUserId,
            BorrowRequestUpdateRequest dto
    ) {
        var entity = fetchBorrowRequestOrThrow(id);
        checkUnauthorizedToChangeRequestStatus(currentUserId, entity);

        var status = fetchStatusOrThrow(dto.statusId());
        checkStatusTransitionAllowed(
                entity.getListing().getId(),
                entity.getStatus().getId(),
                status.getId());

        mapper.applyUpdate(entity, dto, status);
        return mapper.toSummary(entity);
    }

    @Override
    public BorrowRequestSummaryResponse patch(
            Long id,
            Long currentUserId,
            BorrowRequestPatchRequest dto
    ) {
        var entity = fetchBorrowRequestOrThrow(id);
        checkUnauthorizedToChangeRequestStatus(currentUserId, entity);

        RequestStatus status = null;
        if (dto.statusId() != null) {
            status = fetchStatusOrThrow(dto.statusId());
            checkStatusTransitionAllowed(
                    entity.getListing().getId(),
                    entity.getStatus().getId(),
                    status.getId());
        }
        mapper.applyPatch(entity, dto, status);
        return mapper.toSummary(entity);
    }

    @Override
    public void softDelete(Long id, Long currentUserId) {
        var entity = fetchBorrowRequestOrThrow(id);
        Long borrowerId = entity.getBorrower().getId();

        checkRequestOwnerOrThrow(currentUserId, borrowerId);
        checkDeletePermission(currentUserId, borrowerId);

        entity.setDeletedBy(fetchActiveUserOrThrow(currentUserId).getEmail());
        entity.setDeletedAt(LocalDateTime.now());
        entity.setIsDeleted(true);
    }

    @Override
    public void hardDelete(Long id, Long currentUserId) {
        var entity = fetchBorrowRequestOrThrow(id);
        checkDeletePermission(currentUserId, entity.getBorrower().getId());
        repo.deleteById(id);
    }

    private BorrowRequest fetchBorrowRequestOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("BorrowRequest with id=" + id + "not found"));
    }

    private RequestStatus fetchStatusOrThrow(Integer statusId) {
        return statusRepo.findById(statusId)
                .orElseThrow(() -> new NotFoundException("RequestStatus with id=" + statusId + "not found"));
    }

    private AppUser fetchActiveUserOrThrow(Long borrowerId) {
        return userRepo.findByIdAndIsDeletedFalse(borrowerId)
                .orElseThrow(() -> new NotFoundException("User with id=" + borrowerId + "not found"));
    }

    private Listing fetchActiveListingOrThrow(Long listingId) {
        return listingRepo.findByIdAndIsDeletedFalse(listingId)
                .orElseThrow(() -> new NotFoundException("Listing with id=" + listingId + "not found"));
    }

    private void checkUnauthorizedToChangeRequestStatus(
            Long currentUserId,
            BorrowRequest request
    ) {
        boolean isOwner = request.getListing().getBook().getOwner().getId().equals(currentUserId);
        boolean isAdmin = fetchActiveUserOrThrow(currentUserId).getRole().getCode().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException(
                    "You don't have authority to change the status of this request");
        }
    }

    private void checkStatusTransitionAllowed(Long listingId, int fromStatusId, int toStatusId) {
        if (fromStatusId == STATUS_PENDING) {
            checkDuplicateApprovedRequest(listingId);
        }

        boolean allowed =
                (fromStatusId == STATUS_PENDING && (toStatusId == STATUS_APPROVED || toStatusId == STATUS_REJECTED)) ||
                        (fromStatusId == STATUS_APPROVED && toStatusId == STATUS_RETURNED);

        if (!allowed) {
            throw new InvalidStatusTransitionException(fromStatusId, toStatusId);
        }
    }

    private void checkDuplicateApprovedRequest(Long listingId) {
        if (repo.existsByListingIdAndStatusId(listingId, STATUS_APPROVED)) {
            throw new DuplicateApprovedBorrowRequestException(listingId);
        }
    }

    private void checkRequestOwnerOrThrow(Long currentUserId, Long borrowerId) {
        if (!borrowerId.equals(currentUserId)) {
            throw new AccessDeniedException("This request does not belong to you.");
        }
    }

    private void checkDeletePermission(Long currentUserId, Long borrowerId) {
        if (!fetchActiveUserOrThrow(currentUserId).getRole().getCode().equals("ADMIN") && !borrowerId.equals(currentUserId))
            throw new AccessDeniedException("You don't have enough authorities to delete this borrow request");

    }
}
