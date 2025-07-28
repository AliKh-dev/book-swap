package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.borrowrequest.request.*;
import com.alikh.bookswap.dto.borrowrequest.response.*;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.BorrowRequest;
import com.alikh.bookswap.entity.Listing;
import com.alikh.bookswap.entity.RequestStatus;
import com.alikh.bookswap.exception.AccessDeniedException;
import com.alikh.bookswap.exception.BorrowRequestStatusConflictException;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.exception.UnauthorizedStatusChangeException;
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

    private final static int INITIAL_STATUS_ID = 1;
    private final static int APPROVED_STATUS_ID = 2;

    private final BorrowRequestRepository repo;
    private final ListingRepository listingRepo;
    private final RequestStatusRepository statusRepo;
    private final UserRepository userRepo;
    private final BorrowRequestMapper mapper;

    @Override
    public BorrowRequestSummaryResponse create(
            BorrowRequestCreateRequest dto,
            Long borrowerId
    ) {
        var listing = fetchActiveListingOrThrow(dto.listingId());
        if (listing.getBook().getOwner().getId().equals(borrowerId))
            // TODO: I should change the exception type & message
            throw new AccessDeniedException("This book belong to you.");

        var borrower = fetchActiveUserOrThrow(borrowerId);
        var status = fetchStatusOrThrow(INITIAL_STATUS_ID);

        var entity = mapper.fromCreate(dto, listing, borrower, status);
        repo.save(entity);
        return mapper.toSummary(entity);
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
    public List<BorrowRequestSummaryResponse> list(Long borrowerId) {
        return repo.findByBorrowerIdAndIsDeletedFalse(borrowerId).stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRequestSummaryResponse> listRelatedToOwner(Long bookOwnerId) {
        return repo.findByListingBookOwnerIdAndIsDeletedFalse(bookOwnerId).stream()
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
        checkDuplicateApprovedRequest(entity, status);

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
            checkDuplicateApprovedRequest(entity, status);
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
                .orElseThrow(() -> new NotFoundException("BorrowRequest", id));
    }

    private RequestStatus fetchStatusOrThrow(Integer dto) {
        return statusRepo.findById(dto)
                .orElseThrow(() -> new NotFoundException("RequestStatus", dto));
    }

    private AppUser fetchActiveUserOrThrow(Long borrowerId) {
        return userRepo.findByIdAndIsDeletedFalse(borrowerId)
                .orElseThrow(() -> new NotFoundException("User", borrowerId));
    }

    private Listing fetchActiveListingOrThrow(Long listingId) {
        return listingRepo.findByIdAndIsDeletedFalse(listingId)
                .orElseThrow(() -> new NotFoundException("Listing", listingId));
    }

    private void checkUnauthorizedToChangeRequestStatus(Long currentUserId, BorrowRequest entity) {
        if (!entity.getListing().getBook().getOwner().getId().equals(currentUserId) ||
                !fetchActiveUserOrThrow(currentUserId).getRole().getCode().equals("ADMIN"))
            throw new UnauthorizedStatusChangeException("You don't have enough authorities to change status of this request");
    }

    private void checkDuplicateApprovedRequest(BorrowRequest entity, RequestStatus status) {
        if (repo.existsByListingIdAndStatusId(entity.getListing().getId(), APPROVED_STATUS_ID) &&
                status.getId().equals(APPROVED_STATUS_ID))
            throw new BorrowRequestStatusConflictException("You can't have two borrow request with 'APPROVED' status");
    }

    private void checkRequestOwnerOrThrow(Long currentUserId, Long borrowerId) {
        if (!borrowerId.equals(currentUserId))
            throw new AccessDeniedException("This request dose not belong to you.");
    }

    private void checkDeletePermission(Long currentUserId, Long borrowerId) {
        if (!fetchActiveUserOrThrow(currentUserId).getRole().getCode().equals("ADMIN") && !borrowerId.equals(currentUserId))
            throw new AccessDeniedException("You don't have enough authorities to delete this user");

    }


}
