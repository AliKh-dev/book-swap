package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.listingtype.request.*;
import com.alikh.bookswap.dto.listingtype.response.*;
import com.alikh.bookswap.entity.ListingType;
import com.alikh.bookswap.exception.DuplicateCodeException;
import com.alikh.bookswap.exception.parents.NotFoundException;
import com.alikh.bookswap.mapper.ListingTypeMapper;
import com.alikh.bookswap.repository.ListingTypeRepository;
import com.alikh.bookswap.service.contract.ListingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ListingTypeServiceImpl implements ListingTypeService {

    private final ListingTypeRepository repo;
    private final ListingTypeMapper mapper;

    @Override
    public ListingTypeSummaryResponse create(ListingTypeCreateRequest dto) {
        Integer nextId = repo.findTopByOrderByIdDesc()
                .map(ListingType::getId)
                .orElse(0) + 1;
        checkCodeUniquenessOrThrow(dto.code());
        var entity = mapper.fromCreate(dto, nextId);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListingTypeSummaryResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ListingTypeDetailResponse get(Integer id) {
        return mapper.toDetail(fetchListingTypeOrThrow(id));
    }

    @Override
    public ListingTypeSummaryResponse update(Integer id, ListingTypeUpdateRequest dto) {
        var entity = fetchListingTypeOrThrow(id);
        checkCodeUniquenessOrThrow(dto.code());
        mapper.applyUpdate(entity, dto);
        return mapper.toSummary(entity);
    }

    @Override
    public ListingTypeSummaryResponse patch(Integer id, ListingTypePatchRequest dto) {
        var entity = fetchListingTypeOrThrow(id);
        if (dto.code() != null)
            checkCodeUniquenessOrThrow(dto.code());
        mapper.applyPatch(entity, dto);
        return mapper.toSummary(entity);
    }

    @Override
    public void delete(Integer id) {
        fetchListingTypeOrThrow(id);
        repo.deleteById(id);
    }

    private ListingType fetchListingTypeOrThrow(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("ListingType with id=" + id + "not found"));
    }

    private void checkCodeUniquenessOrThrow(String code) {
        if (repo.existsByCode(code))
            throw new DuplicateCodeException("ListingType", code);
    }
}
