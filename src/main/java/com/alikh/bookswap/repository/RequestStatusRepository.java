package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.RequestStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestStatusRepository extends JpaRepository<RequestStatus, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RequestStatus> findTopByOrderByIdDesc();
}
