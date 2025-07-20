package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {
}
