package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Page<AppUser> findByRole(Role role, Pageable pageable);
}
