package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.book.request.*;
import com.alikh.bookswap.dto.book.response.*;
import com.alikh.bookswap.entity.*;
import com.alikh.bookswap.exception.AccessDeniedException;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.mapper.BookMapper;
import com.alikh.bookswap.repository.*;

import com.alikh.bookswap.service.contract.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository repo;
    private final BookMapper mapper;
    private final UserRepository userRepo;
    private final BookConditionRepository conditionRepo;

    @Override
    public BookSummaryResponse create(BookCreateRequest dto, Long ownerId) {
        var condition = fetchBookConditionOrThrow(dto.conditionId());
        var owner = fetchUserOrThrow(ownerId);

        Book entity = mapper.fromCreate(dto, condition, owner);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional
    public List<BookSummaryResponse> list() {
        return repo.findByIsDeletedFalse().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookSummaryResponse> listMine(Long ownerId) {
        return repo.findByOwnerIdAndIsDeletedFalse(ownerId).stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookDetailResponse get(Long id) {
        return mapper.toDetail(fetchActiveBookOrThrow(id));
    }

    @Override
    public BookSummaryResponse update(Long id, BookUpdateRequest dto, Long currentUserId) {
        var book = fetchActiveBookOrThrow(id);
        checkBookOwnerOrThrow(currentUserId, book);

        BookCondition condition = fetchBookConditionOrThrow(dto.conditionId());

        mapper.applyUpdate(book, dto, condition);
        return mapper.toSummary(book);
    }

    @Override
    public BookSummaryResponse patch(Long id, BookPatchRequest dto, Long currentUserId) {
        var book = fetchActiveBookOrThrow(id);
        checkBookOwnerOrThrow(currentUserId, book);

        BookCondition condition = null;
        if (dto.conditionId() != null)
            condition = fetchBookConditionOrThrow(dto.conditionId());

        mapper.applyPatch(book, dto, condition);
        return mapper.toSummary(book);
    }

    @Override
    public void softDelete(Long id, Long currentUserId) {
        var book = fetchActiveBookOrThrow(id);
        checkBookOwnerOrThrow(currentUserId, book);

        book.setDeletedBy(fetchUserOrThrow(currentUserId).getEmail());
        book.setDeletedAt(LocalDateTime.now());
        book.setIsDeleted(true);
    }

    @Override
    public void hardDelete(Long id, Long currentUserId) {
        var book = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book", id));
        checkBookOwnerOrThrow(currentUserId, book);
        repo.deleteById(id);
    }

    private Book fetchActiveBookOrThrow(Long id) {
        return repo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Book", id));
    }

    private BookCondition fetchBookConditionOrThrow(Integer id) {
        return conditionRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("BookCondition", id));
    }

    private AppUser fetchUserOrThrow(Long ownerId) {
        return userRepo.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User", ownerId));
    }

    private void checkBookOwnerOrThrow(Long currentUserId, Book book) {
        boolean isOwner = book.getOwner().getId().equals(currentUserId);
        boolean isAdmin = fetchUserOrThrow(currentUserId)
                .getRole().getCode().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You do not own this book");
        }
    }
}
