package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.penaltytype.request.*;
import com.alikh.bookswap.dto.penaltytype.response.*;
import com.alikh.bookswap.entity.PenaltyType;
import com.alikh.bookswap.exception.NotFoundException;
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

        PenaltyType entity = mapper.fromCreate(dto, nextId);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PenaltyTypeDetailResponse get(Integer id) {
        return mapper.toDetail(repo.findById(id)
                .orElseThrow(() -> new NotFoundException("PenaltyType", id)));
    }

    @Override
    public void update(Integer id, PenaltyTypeUpdateRequest dto) {
        PenaltyType entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("PenaltyType", id));
        mapper.applyUpdate(entity, dto);
    }

    @Override
    public void patch(Integer id, PenaltyTypePatchRequest dto) {
        PenaltyType entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("PenaltyType", id));
        mapper.applyPatch(entity, dto);
    }

    @Override
    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new NotFoundException("PenaltyType", id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PenaltyTypeSummaryResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }
}
