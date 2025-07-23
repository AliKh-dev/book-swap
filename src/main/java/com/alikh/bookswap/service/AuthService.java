package com.alikh.bookswap.service;

import com.alikh.bookswap.dto.auth.ChangePasswordRequest;
import com.alikh.bookswap.dto.auth.LoginRequest;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.exception.PasswordNotMatch;
import com.alikh.bookswap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(Long id, ChangePasswordRequest request) {
        AppUser user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User", id));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new PasswordNotMatch();
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
    }

    public AppUser getUserByEmail(LoginRequest request) {
        return userRepo.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("User", request.email()));
    }

    public AppUser getUserByUserId(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
    }
}
