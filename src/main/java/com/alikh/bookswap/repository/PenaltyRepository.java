package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    List<Penalty> findByIsDeletedFalse();

    Optional<Penalty> findByIdAndIsDeletedFalse(Long id);

    boolean existsByRequestId(Long requestId);
}
