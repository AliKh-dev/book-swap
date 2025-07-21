package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.penalty.request.*;
import com.alikh.bookswap.dto.penalty.response.*;
import com.alikh.bookswap.entity.*;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.mapper.PenaltyMapper;
import com.alikh.bookswap.repository.*;

import com.alikh.bookswap.service.contract.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PenaltySummaryResponse create(PenaltyCreateRequest dto) {

        BorrowRequest req = requestRepo.findById(dto.requestId())
                .orElseThrow(() -> new NotFoundException("BorrowRequest", dto.requestId()));

        PenaltyType type = typeRepo.findById(dto.typeId())
                .orElseThrow(() -> new NotFoundException("PenaltyType", dto.typeId()));

        Penalty entity = mapper.fromCreate(dto, req, type);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PenaltyDetailResponse get(Long id) {
        Penalty p = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Penalty", id));
        return mapper.toDetail(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PenaltySummaryResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public void update(Long id, PenaltyUpdateRequest dto) {

        Penalty p = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Penalty", id));

        PenaltyType type = typeRepo.findById(dto.typeId())
                .orElseThrow(() -> new NotFoundException("PenaltyType", dto.typeId()));

        AppUser resolver = userRepo.findById(dto.resolvedById())
                .orElseThrow(() -> new NotFoundException("User", dto.resolvedById()));

        mapper.applyUpdate(p, dto, type, resolver);
    }

    @Override
    public void patch(Long id, PenaltyPatchRequest dto) {

        Penalty penalty = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Penalty", id));

        PenaltyType type = penalty.getType();
        if (dto.typeId() != null) {
            type = typeRepo.findById(dto.typeId())
                    .orElseThrow(() -> new NotFoundException("PenaltyType", dto.typeId()));
        }

        AppUser resolver = penalty.getResolvedBy();
        if (dto.resolvedById() != null) {
            resolver = userRepo.findById(dto.resolvedById())
                    .orElseThrow(() -> new NotFoundException("User", dto.resolvedById()));
        }

        mapper.applyPatch(penalty, dto, type, resolver);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new NotFoundException("Penalty", id);
        repo.deleteById(id);
    }
}
