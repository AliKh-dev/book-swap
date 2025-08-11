package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.penalty.request.*;
import com.alikh.bookswap.dto.penalty.response.*;
import com.alikh.bookswap.entity.*;
import com.alikh.bookswap.exception.AccessDeniedException;
import com.alikh.bookswap.exception.DuplicatePenaltyException;
import com.alikh.bookswap.exception.parents.NotFoundException;
import com.alikh.bookswap.mapper.PenaltyMapper;
import com.alikh.bookswap.repository.*;

import com.alikh.bookswap.service.contract.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PenaltyServiceImpl implements PenaltyService {

    private final PenaltyRepository repo;
    private final PenaltyTypeRepository typeRepo;
    private final BorrowRequestRepository requestRepo;
    private final UserRepository userRepo;
    private final PenaltyMapper mapper;

    @Override
    public PenaltySummaryResponse create(PenaltyCreateRequest dto, Long reqId) {
        if (repo.existsByRequestId(reqId)) {
            throw new DuplicatePenaltyException(reqId);
        }
        var request = fetchActiveRequestOrThrow(reqId);
        var type = fetchPenaltyTypeOrThrow(dto.typeId());

        var entity = mapper.fromCreate(dto, request, type);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PenaltySummaryResponse> list() {
        return repo.findByIsDeletedFalse().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PenaltyDetailResponse get(Long id) {
        return mapper.toDetail(fetchActivePenaltyOrThrow(id));
    }

    @Override
    public PenaltySummaryResponse update(
            Long id,
            PenaltyUpdateRequest dto,
            Long currentUserId
    ) {
        var entity = fetchActivePenaltyOrThrow(id);
        var type = fetchPenaltyTypeOrThrow(dto.typeId());
        var resolver = fetchActiveUserOrThrow(currentUserId);

        mapper.applyUpdate(entity, dto, type, resolver);
        return mapper.toSummary(entity);
    }

    @Override
    public PenaltySummaryResponse patch(
            Long id,
            PenaltyPatchRequest dto,
            Long currentUserId
    ) {
        var entity = fetchActivePenaltyOrThrow(id);

        PenaltyType type = null;
        if (dto.typeId() != null) {
            type = fetchPenaltyTypeOrThrow(dto.typeId());
        }

        AppUser resolver = null;
        if (dto.resolvedById() != null) {
            resolver = fetchActiveUserOrThrow(dto.resolvedById());
        }

        mapper.applyPatch(entity, dto, type, resolver);
        return mapper.toSummary(entity);
    }

    @Override
    public void softDelete(Long id, Long currentUserId) {
        var entity = fetchActivePenaltyOrThrow(id);
        checkDeletePermission(currentUserId);

        entity.setDeletedBy(fetchActiveUserOrThrow(currentUserId).getEmail());
        entity.setDeletedAt(LocalDateTime.now());
        entity.setIsDeleted(true);
    }

    @Override
    public void hardDelete(Long id, Long currentUserId) {
        fetchActivePenaltyOrThrow(id);
        checkDeletePermission(currentUserId);
        repo.deleteById(id);
    }

    private BorrowRequest fetchActiveRequestOrThrow(Long requestId) {
        return requestRepo.findByIdAndIsDeletedFalse(requestId)
                .orElseThrow(() -> new NotFoundException("BorrowRequest with id=" + requestId + "not found"));
    }

    private PenaltyType fetchPenaltyTypeOrThrow(Integer typeId) {
        return typeRepo.findById(typeId)
                .orElseThrow(() -> new NotFoundException("PenaltyType with id=" + typeId + "not found"));
    }

    private Penalty fetchActivePenaltyOrThrow(Long id) {
        return repo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Penalty with id=" + id + "not found"));
    }

    private AppUser fetchActiveUserOrThrow(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + "not found"));
    }

    private void checkDeletePermission(Long currentUserId) {
        if (!isAdmin(currentUserId)) {
            throw new AccessDeniedException("You don't have authority to delete this penalty");
        }
    }

    private boolean isAdmin(Long userId) {
        return "ADMIN".equals(fetchActiveUserOrThrow(userId).getRole().getCode());
    }
}
