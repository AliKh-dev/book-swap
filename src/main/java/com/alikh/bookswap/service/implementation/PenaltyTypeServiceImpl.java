package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.penaltytype.request.*;
import com.alikh.bookswap.dto.penaltytype.response.*;
import com.alikh.bookswap.entity.PenaltyType;
import com.alikh.bookswap.exception.DuplicateCodeException;
import com.alikh.bookswap.exception.parents.NotFoundException;
import com.alikh.bookswap.mapper.PenaltyTypeMapper;
import com.alikh.bookswap.repository.PenaltyTypeRepository;
import com.alikh.bookswap.service.contract.PenaltyTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class PenaltyTypeServiceImpl implements PenaltyTypeService {

    private final PenaltyTypeRepository repo;
    private final PenaltyTypeMapper mapper;

    @Override
    public PenaltyTypeSummaryResponse create(PenaltyTypeCreateRequest dto) {
        Integer nextId = repo.findTopByOrderByIdDesc()
                .map(PenaltyType::getId)
                .orElse(0) + 1;
        checkCodeUniquenessOrThrow(dto.code());
        var entity = mapper.fromCreate(dto, nextId);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PenaltyTypeSummaryResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PenaltyTypeDetailResponse get(Integer id) {
        return mapper.toDetail(fetchPenaltyTypeOrThrow(id));
    }

    @Override
    public PenaltyTypeSummaryResponse update(Integer id, PenaltyTypeUpdateRequest dto) {
        var entity = fetchPenaltyTypeOrThrow(id);
        checkCodeUniquenessOrThrow(dto.code());
        mapper.applyUpdate(entity, dto);
        return mapper.toSummary(entity);
    }

    @Override
    public PenaltyTypeSummaryResponse patch(Integer id, PenaltyTypePatchRequest dto) {
        PenaltyType entity = fetchPenaltyTypeOrThrow(id);
        if (dto.code() != null)
            checkCodeUniquenessOrThrow(dto.code());
        mapper.applyPatch(entity, dto);
        return mapper.toSummary(entity);
    }

    @Override
    public void delete(Integer id) {
        fetchPenaltyTypeOrThrow(id);
        repo.deleteById(id);
    }

    private PenaltyType fetchPenaltyTypeOrThrow(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("PenaltyType with id=" + id + "not found"));
    }

    private void checkCodeUniquenessOrThrow(String dto) {
        if (repo.existsByCode(dto))
            throw new DuplicateCodeException("PenaltyType", dto);
    }
}
