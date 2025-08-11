package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.bookcondition.request.*;
import com.alikh.bookswap.dto.bookcondition.response.*;
import com.alikh.bookswap.entity.BookCondition;
import com.alikh.bookswap.exception.DuplicateCodeException;
import com.alikh.bookswap.exception.parents.NotFoundException;
import com.alikh.bookswap.mapper.BookConditionMapper;
import com.alikh.bookswap.repository.BookConditionRepository;
import com.alikh.bookswap.service.contract.BookConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class BookConditionServiceImpl implements BookConditionService {

    private final BookConditionRepository repo;
    private final BookConditionMapper mapper;

    @Override
    public BookConditionSummaryResponse create(BookConditionCreateRequest dto) {
        Integer nextId = repo.findTopByOrderByIdDesc()
                .map(BookCondition::getId)
                .orElse(0) + 1;
        checkCodeUniquenessOrThrow(dto.code());
        var entity = mapper.fromCreate(dto, nextId);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookConditionSummaryResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookConditionDetailResponse get(Integer id) {
        return mapper.toDetail(fetchBookConditionOrThrow(id));
    }

    @Override
    public BookConditionSummaryResponse update(Integer id, BookConditionUpdateRequest dto) {
        var entity = fetchBookConditionOrThrow(id);
        checkCodeUniquenessOrThrow(dto.code());
        mapper.applyUpdate(entity, dto);
        return mapper.toSummary(entity);
    }

    @Override
    public BookConditionSummaryResponse patch(Integer id, BookConditionPatchRequest dto) {
        var entity = fetchBookConditionOrThrow(id);
        if (dto.code() != null)
            checkCodeUniquenessOrThrow(dto.code());
        mapper.applyPatch(entity, dto);
        return mapper.toSummary(entity);
    }

    @Override
    public void delete(Integer id) {
        fetchBookConditionOrThrow(id);
        repo.deleteById(id);
    }

    private BookCondition fetchBookConditionOrThrow(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("BookCondition with id=" + id + "not found"));
    }

    private void checkCodeUniquenessOrThrow(String code) {
        if (repo.existsByCode(code))
            throw new DuplicateCodeException("BookCondition", code);
    }
}
