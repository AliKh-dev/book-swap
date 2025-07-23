package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.ListingType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListingTypeRepository extends JpaRepository<ListingType, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ListingType> findTopByOrderByIdDesc();

    boolean existsByCode(String code);
}
