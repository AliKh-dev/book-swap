package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.ListingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingTypeRepository extends JpaRepository<ListingType, Integer> {
}
