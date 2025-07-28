package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    List<Listing> findByIsDeletedFalse();

    List<Listing> findByBookOwnerIdAndIsDeletedFalse(Long bookOwnerId);

    Optional<Listing> findByIdAndIsDeletedFalse(Long id);
}
