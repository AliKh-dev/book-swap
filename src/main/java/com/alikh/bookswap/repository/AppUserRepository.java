package com.alikh.bookswap.repository;

import com.alikh.bookswap.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}
