package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.PenaltyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyTypeRepository extends JpaRepository<PenaltyType, Integer> {
}
