package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.bookcondition.request.*;
import com.alikh.bookswap.dto.bookcondition.response.*;
import com.alikh.bookswap.entity.BookCondition;
import com.alikh.bookswap.exception.NotFoundException;
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

        BookCondition entity = mapper.fromCreate(dto, nextId);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public BookConditionDetailResponse get(Integer id) {
        return mapper.toDetail(repo.findById(id)
                .orElseThrow(() -> new NotFoundException("BookCondition", id)));
    }

    @Override
    public void update(Integer id, BookConditionUpdateRequest dto) {
        BookCondition entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("BookCondition", id));
        mapper.applyUpdate(entity, dto);
    }

    @Override
    public void patch(Integer id, BookConditionPatchRequest dto) {
        BookCondition entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("BookCondition", id));
        mapper.applyPatch(entity, dto);
    }

    @Override
    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new NotFoundException("BookCondition", id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookConditionSummaryResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }
}
