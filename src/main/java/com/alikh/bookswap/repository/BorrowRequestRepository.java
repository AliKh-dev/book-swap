package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.BorrowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Long> {

    List<BorrowRequest> findByIsDeletedFalse();

    Optional<BorrowRequest> findByIdAndIsDeletedFalse(Long id);
}
