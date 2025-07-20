package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestStatusRepository extends JpaRepository<RequestStatus, Integer> {
}
