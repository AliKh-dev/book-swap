package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.borrowrequest.request.*;
import com.alikh.bookswap.dto.borrowrequest.response.*;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.BorrowRequest;
import com.alikh.bookswap.entity.Listing;
import com.alikh.bookswap.entity.RequestStatus;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.mapper.BorrowRequestMapper;
import com.alikh.bookswap.repository.BorrowRequestRepository;
import com.alikh.bookswap.repository.ListingRepository;
import com.alikh.bookswap.repository.RequestStatusRepository;
import com.alikh.bookswap.repository.UserRepository;
import com.alikh.bookswap.service.contract.BorrowRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowRequestServiceImpl implements BorrowRequestService {

    private final BorrowRequestRepository repo;
    private final ListingRepository listingRepo;
    private final RequestStatusRepository statusRepo;
    private final UserRepository userRepo;
    private final BorrowRequestMapper mapper;

    @Override
    public BorrowRequestSummaryResponse create(BorrowRequestCreateRequest dto) {

        Listing listing = listingRepo.findById(dto.listingId())
                .orElseThrow(() -> new NotFoundException("Listing", dto.listingId()));

        AppUser borrower = userRepo.findById(dto.borrowerId())
                .orElseThrow(() -> new NotFoundException("User", dto.borrowerId()));

        RequestStatus initStatus = statusRepo.findById(dto.statusId())
                .orElseThrow(() -> new NotFoundException("RequestStatus", dto.statusId()));

        BorrowRequest entity = mapper.fromCreate(dto, null, listing, borrower, initStatus);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public BorrowRequestDetailResponse get(Long id) {
        BorrowRequest req = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("BorrowRequest", id));
        return mapper.toDetail(req);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRequestSummaryResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public void update(Long id, BorrowRequestUpdateRequest dto) {

        BorrowRequest req = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("BorrowRequest", id));

        RequestStatus status = statusRepo.findById(dto.statusId())
                .orElseThrow(() -> new NotFoundException("RequestStatus", dto.statusId()));

        mapper.applyUpdate(req, dto, status);
    }

    @Override
    public void patch(Long id, BorrowRequestPatchRequest dto) {

        BorrowRequest br = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("BorrowRequest", id));

        RequestStatus status = null;
        if (dto.statusId() != null) {
            status = statusRepo.findById(dto.statusId())
                    .orElseThrow(() -> new NotFoundException("RequestStatus", dto.statusId()));
        }

        mapper.applyPatch(br, dto, status);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new NotFoundException("BorrowRequest", id);
        repo.deleteById(id);
    }
}
