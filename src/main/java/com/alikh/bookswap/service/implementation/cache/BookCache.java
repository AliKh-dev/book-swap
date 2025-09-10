package com.alikh.bookswap.service.implementation.cache;

import com.alikh.bookswap.dto.book.cache.BookCacheView;
import com.alikh.bookswap.entity.Book;
import com.alikh.bookswap.mapper.BookMapper;
import com.alikh.bookswap.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookCache {

    private final BookRepository repo;
    private final BookMapper mapper;

    @Cacheable(value = "bookById", key = "#id")
    @Transactional(readOnly = true)
    public BookCacheView getOrLoad(Long id) {
        return repo.findByIdAndIsDeletedFalse(id)
                .map(mapper::toCacheView)
                .orElse(null);
    }

    @CachePut(value = "bookById", key = "#view.id")
    public BookCacheView put(BookCacheView view) {
        return view;
    }

    @CacheEvict(value = "bookById", key = "#id")
    public void evict(Long id) { /* no-op */ }
}
