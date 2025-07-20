package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
