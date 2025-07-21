package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.BookCondition;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookConditionRepository extends JpaRepository<BookCondition, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BookCondition> findTopByOrderByIdDesc();
}
