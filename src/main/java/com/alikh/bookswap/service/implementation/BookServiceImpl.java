package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.book.request.*;
import com.alikh.bookswap.dto.book.response.*;
import com.alikh.bookswap.entity.*;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.mapper.BookMapper;
import com.alikh.bookswap.repository.*;

import com.alikh.bookswap.service.contract.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final BookConditionRepository conditionRepo;
    private final BookMapper mapper;

    @Override
    public BookSummaryResponse create(BookCreateRequest dto) {

        BookCondition condition = conditionRepo.findById(dto.conditionId())
                .orElseThrow(() -> new NotFoundException("BookCondition", dto.conditionId()));

        AppUser owner = userRepo.findById(dto.ownerId())
                .orElseThrow(() -> new NotFoundException("User", dto.ownerId()));

        Book entity = mapper.fromCreate(dto, condition, owner);
        bookRepo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDetailResponse get(Long id) {
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book", id));
        return mapper.toDetail(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookSummaryResponse> list() {
        return bookRepo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public void update(Long id, BookUpdateRequest dto) {
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book", id));

        BookCondition condition = conditionRepo.findById(dto.conditionId())
                .orElseThrow(() -> new NotFoundException("BookCondition", dto.conditionId()));

        mapper.applyUpdate(book, dto, condition);
        // dirty-checking will flush changes at commit
    }

    @Override
    public void patch(Long id, BookPatchRequest dto) {
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book", id));

        BookCondition condition = book.getCondition();
        if (dto.conditionId() != null) {
            condition = conditionRepo.findById(dto.conditionId())
                    .orElseThrow(() -> new NotFoundException("BookCondition", dto.conditionId()));
        }

        mapper.applyPatch(book, dto, condition);
    }

    @Override
    public void delete(Long id) {
        if (!bookRepo.existsById(id)) {
            throw new NotFoundException("Book", id);
        }
        bookRepo.deleteById(id);
    }
}
