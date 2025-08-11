package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.user.request.*;
import com.alikh.bookswap.dto.user.response.UserDetailResponse;
import com.alikh.bookswap.dto.user.response.UserSummaryResponse;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.exception.*;
import com.alikh.bookswap.exception.parents.NotFoundException;
import com.alikh.bookswap.mapper.UserMapper;
import com.alikh.bookswap.repository.UserRepository;
import com.alikh.bookswap.service.contract.RoleService;
import com.alikh.bookswap.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private static final int DEFAULT_ROLE_ID = 2;

    private final UserMapper mapper;
    private final UserRepository repo;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser create(UserCreateRequest dto) {
        checkEmailUniquenessOrThrow(dto.email());
        var role = roleService.get(DEFAULT_ROLE_ID);
        var entity = mapper.fromCreate(dto, role, passwordEncoder.encode(dto.password()));

        repo.save(entity);
        return entity;
    }

    public AppUser fetch(Long id) {
        return repo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + "not found"));
    }

    public AppUser fetch(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email=" + email + "not found"));
    }

    @Override
    public AppUser authenticate(UserLoginRequest dto) {
        var user = fetch(dto.email());

        verifyPassword(dto.password(), user.getPassword());
        return user;
    }

    public void changePassword(UserChangePasswordRequest dto) {
        var user = fetch(dto.userId());

        verifyPassword(dto.currentPassword(), user.getPassword());
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryResponse> list() {
        return repo.findByIsDeletedFalse().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailResponse get(Long id) {
        return mapper.toDetail(fetch(id));
    }

    @Override
    public UserSummaryResponse update(Long id, UserUpdateRequest dto) {
        checkEmailUniquenessOrThrow(dto.email());

        var user = fetch(id);

        mapper.applyUpdate(user, dto);
        return mapper.toSummary(user);
    }

    @Override
    public UserSummaryResponse patch(Long id, UserPatchRequest dto) {
        if (dto.email() != null)
            checkEmailUniquenessOrThrow(dto.email());

        var user = fetch(id);

        mapper.applyPatch(user, dto);
        return mapper.toSummary(user);
    }

    @Override
    public UserSummaryResponse changeRole(Long id, UserRoleChangeRequest dto) {
        var entity = fetch(id);
        var role = roleService.get(dto.roleId());

        entity.setRole(role);
        return mapper.toSummary(entity);
    }

    @Override
    public void softDelete(Long id, Long currentUserId) {
        var user = fetch(id);

        checkDeletePermission(id, currentUserId);

        user.setDeletedBy(fetch(currentUserId).getEmail());
        user.setDeletedAt(LocalDateTime.now());
        user.setIsDeleted(true);
    }

    @Override
    public void hardDeleted(Long id, Long currentUserId) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("User with id=" + id + "not found");
        }
        checkDeletePermission(id, currentUserId);

        repo.deleteById(id);
    }

    private void checkEmailUniquenessOrThrow(String email) {
        if (repo.existsByEmail(email))
            throw new DuplicateEmailException(email);
    }

    private void verifyPassword(String rawPassword, String hashedPassword) {
        if (!passwordEncoder.matches(rawPassword, hashedPassword)) {
            throw new BadCredentialsException();
        }
    }

    private void checkDeletePermission(Long id, Long currentUserId) {
        if (!this.fetch(currentUserId).getRole().getCode().equals("ADMIN") && !id.equals(currentUserId))
            throw new AccessDeniedException("You don't have enough authorities to delete this user");
    }
}
