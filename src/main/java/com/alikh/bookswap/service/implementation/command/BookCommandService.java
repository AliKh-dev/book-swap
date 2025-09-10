package com.alikh.bookswap.service.implementation.command;

import com.alikh.bookswap.dto.book.request.BookCreateRequest;
import com.alikh.bookswap.dto.book.request.BookPatchRequest;
import com.alikh.bookswap.dto.book.request.BookUpdateRequest;
import com.alikh.bookswap.dto.book.response.BookSummaryResponse;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.Book;
import com.alikh.bookswap.entity.BookCondition;
import com.alikh.bookswap.exception.AccessDeniedException;
import com.alikh.bookswap.exception.parents.NotFoundException;
import com.alikh.bookswap.mapper.BookMapper;
import com.alikh.bookswap.repository.BookConditionRepository;
import com.alikh.bookswap.repository.BookRepository;
import com.alikh.bookswap.repository.UserRepository;
import com.alikh.bookswap.service.implementation.cache.BookCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCommandService {

    private final BookRepository repo;
    private final BookMapper mapper;
    private final BookCache cache;
    private final UserRepository userRepo;
    private final BookConditionRepository conditionRepo;

    public BookSummaryResponse create(BookCreateRequest dto, Long ownerId) {
        var condition = fetchBookConditionOrThrow(dto.conditionId());
        var owner = fetchUserOrThrow(ownerId);

        var entity = mapper.fromCreate(dto, condition, owner);
        repo.save(entity);

        cache.put(mapper.toCacheView(entity));
        return mapper.toSummary(entity);
    }

    public BookSummaryResponse update(Long id, BookUpdateRequest dto, Long currentUserId) {
        var book = fetchActiveBookOrThrow(id);
        checkBookOwnerOrThrow(currentUserId, book);

        var condition = fetchBookConditionOrThrow(dto.conditionId());
        mapper.apply(book, dto, condition);

        cache.put(mapper.toCacheView(book));
        return mapper.toSummary(book);
    }

    public BookSummaryResponse patch(Long id, BookPatchRequest dto, Long currentUserId) {
        var book = fetchActiveBookOrThrow(id);
        checkBookOwnerOrThrow(currentUserId, book);

        BookCondition condition = null;
        if (dto.conditionId() != null) {
            condition = fetchBookConditionOrThrow(dto.conditionId());
        }
        mapper.apply(book, dto, condition);

        cache.put(mapper.toCacheView(book));
        return mapper.toSummary(book);
    }

    public void softDelete(Long id, Long currentUserId) {
        var book = fetchActiveBookOrThrow(id);
        checkBookOwnerOrThrow(currentUserId, book);

        book.setDeletedBy(fetchUserOrThrow(currentUserId).getEmail());
        book.setDeletedAt(LocalDateTime.now());
        book.setIsDeleted(true);

        cache.evict(id);
    }

    public void hardDelete(Long id, Long currentUserId) {
        var book = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id=" + id + " not found"));
        checkBookOwnerOrThrow(currentUserId, book);

        repo.deleteById(id);
        cache.evict(id);
    }

    private Book fetchActiveBookOrThrow(Long id) {
        return repo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Book with id=" + id + " not found"));
    }

    private BookCondition fetchBookConditionOrThrow(Integer id) {
        return conditionRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("BookCondition with id=" + id + " not found"));
    }

    private AppUser fetchUserOrThrow(Long ownerId) {
        return userRepo.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id=" + ownerId + " not found"));
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

