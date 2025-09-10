package com.alikh.bookswap.service.implementation.query;

import com.alikh.bookswap.dto.book.response.BookDetailResponse;
import com.alikh.bookswap.dto.book.response.BookSummaryResponse;
import com.alikh.bookswap.exception.parents.NotFoundException;
import com.alikh.bookswap.mapper.BookMapper;
import com.alikh.bookswap.repository.BookRepository;
import com.alikh.bookswap.service.implementation.cache.BookCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookQueryService {

    private final BookRepository repo;
    private final BookMapper mapper;
    private final BookCache cache;

    public List<BookSummaryResponse> list() {
        return repo.findByIsDeletedFalse().stream()
                .map(mapper::toSummary)
                .toList();
    }

    public List<BookSummaryResponse> listMine(Long ownerId) {
        return repo.findByOwnerIdAndIsDeletedFalse(ownerId).stream()
                .map(mapper::toSummary)
                .toList();
    }

    public BookDetailResponse get(Long id) {
        var view = cache.getOrLoad(id);
        if (view == null) {
            throw new NotFoundException("Book with id=" + id + " not found");
        }
        return mapper.toDetail(view);
    }
}