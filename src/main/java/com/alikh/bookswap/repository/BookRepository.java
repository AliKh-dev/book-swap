package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByOwnerIdAndIsDeletedFalse(Long ownerId);
    
    Optional<Book> findByIdAndIsDeletedFalse(Long id);
}
