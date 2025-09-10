package com.alikh.bookswap.mapper;

import com.alikh.bookswap.dto.book.cache.BookCacheView;
import com.alikh.bookswap.dto.book.request.*;
import com.alikh.bookswap.dto.book.response.*;
import com.alikh.bookswap.entity.Book;
import com.alikh.bookswap.entity.BookCondition;
import com.alikh.bookswap.entity.AppUser;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    /* ---------- Entity -> DTO ---------- */

    public BookSummaryResponse toSummary(Book book) {
        return new BookSummaryResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn()
        );
    }

    public BookDetailResponse toDetail(Book book) {
        return new BookDetailResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn(),
                book.getDescription(),
                book.getCondition() != null ? book.getCondition().getCode() : null,
                book.getOwner() != null ? book.getOwner().getName() : null,
                book.getCreatedAt()
        );
    }

    public BookDetailResponse toDetail(BookCacheView cacheView) {
        return new BookDetailResponse(
                cacheView.id(),
                cacheView.title(),
                cacheView.author(),
                cacheView.genre(),
                cacheView.isbn(),
                cacheView.description(),
                cacheView.condition(),
                cacheView.ownerName(),
                cacheView.createdAt()
        );
    }

    public BookCacheView toCacheView(Book book) {
        return new BookCacheView(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn(),
                book.getDescription(),
                book.getCondition() != null ? book.getCondition().getCode() : null,
                book.getOwner() != null ? book.getOwner().getName() : null,
                book.getCreatedAt()
        );
    }

    /* ---------- Create / Update requests ➜ Entity ---------- */

    public Book fromCreate(BookCreateRequest request,
                           BookCondition condition,
                           AppUser owner) {
        return Book.builder()
                .title(request.title())
                .author(request.author())
                .genre(request.genre())
                .isbn(request.isbn())
                .description(request.description())
                .condition(condition)
                .owner(owner)
                .build();
    }

    public void apply(Book book,
                      BookUpdateRequest request,
                      BookCondition condition) {
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setGenre(request.genre());
        book.setIsbn(request.isbn());
        book.setDescription(request.description());
        book.setCondition(condition);
    }

    public void apply(Book book,
                      BookPatchRequest request,
                      BookCondition condition) {
        if (request.title() != null) book.setTitle(request.title());
        if (request.author() != null) book.setAuthor(request.author());
        if (request.genre() != null) book.setGenre(request.genre());
        if (request.isbn() != null) book.setIsbn(request.isbn());
        if (request.description() != null) book.setDescription(request.description());
        if (request.conditionId() != null) book.setCondition(condition);
    }
}
