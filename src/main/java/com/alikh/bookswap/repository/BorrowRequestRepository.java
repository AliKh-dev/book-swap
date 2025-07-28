package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.BorrowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Long> {

    boolean existsByListingIdAndStatusId(Long listingId, Integer StatusId);

    List<BorrowRequest> findByIsDeletedFalse();

    List<BorrowRequest> findByBorrowerIdAndIsDeletedFalse(Long borrowerId);

    List<BorrowRequest> findByListingBookOwnerIdAndIsDeletedFalse(Long ownerId);

    Optional<BorrowRequest> findByIdAndIsDeletedFalse(Long id);
}
