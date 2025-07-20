package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.BookCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookConditionRepository extends JpaRepository<BookCondition, Integer> {
}
