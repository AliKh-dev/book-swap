package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.user.request.*;
import com.alikh.bookswap.dto.user.response.UserDetailResponse;
import com.alikh.bookswap.dto.user.response.UserSummaryResponse;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.Role;
import com.alikh.bookswap.exception.AccessDeniedException;
import com.alikh.bookswap.exception.EmailAlreadyExistsException;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.exception.UnauthorizedException;
import com.alikh.bookswap.mapper.UserMapper;
import com.alikh.bookswap.repository.RoleRepository;
import com.alikh.bookswap.repository.UserRepository;
import com.alikh.bookswap.service.contract.UserService;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserSummaryResponse create(UserCreateRequest dto) {
        checkEmailUniquenessOrThrow(dto.email());
        var role = fetchRoleOrThrow(DEFAULT_ROLE_ID);
        var entity = mapper.fromCreate(dto, role, passwordEncoder.encode(dto.password()));

        userRepo.save(entity);
        return mapper.toSummary(entity);
    }

    public AppUser authenticate(UserLoginRequest request) {
        var user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        checkPasswordOrThrow(request.password(), user.getPassword());
        return user;
    }

    public void changePassword(UserChangePasswordRequest request) {
        var user = fetchUserOrThrow(request.userId());
        checkPasswordOrThrow(request.currentPassword(), user.getPassword());
        user.setPassword(passwordEncoder.encode(request.newPassword()));
    }

    public AppUser getEntity(Long id) {
        return fetchUserOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryResponse> list() {
        return userRepo.findByIsDeletedFalse().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailResponse get(Long id) {
        return mapper.toDetail(fetchUserOrThrow(id));
    }

    @Override
    public UserSummaryResponse update(Long id, UserUpdateRequest dto) {
        var user = fetchUserOrThrow(id);

        checkEmailUniquenessOrThrow(dto.email());
        var role = fetchRoleOrThrow(dto.roleId());

        mapper.applyUpdate(user, dto, role);
        return mapper.toSummary(user);
    }

    @Override
    public UserSummaryResponse patch(Long id, UserPatchRequest dto) {
        AppUser user = fetchUserOrThrow(id);

        if (dto.email() != null)
            checkEmailUniquenessOrThrow(dto.email());

        Role role = null;
        if (dto.roleId() != null)
            role = fetchRoleOrThrow(dto.roleId());

        mapper.applyPatch(user, dto, role);
        return mapper.toSummary(user);
    }

    @Override
    public void softDelete(Long id, Long currentUserId) {
        AppUser user = fetchUserOrThrow(id);

        checkDeletePermission(id, currentUserId);

        user.setDeletedBy(fetchUserOrThrow(currentUserId).getEmail());
        user.setDeletedAt(LocalDateTime.now());
        user.setIsDeleted(true);
    }

    @Override
    public void hardDeleted(Long id, Long currentUserId) {
        if (!userRepo.existsById(id)) {
            throw new NotFoundException("User", id);
        }
        checkDeletePermission(id, currentUserId);

        userRepo.deleteById(id);
    }

    private AppUser fetchUserOrThrow(Long id) {
        return userRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("User", id));
    }

    private Role fetchRoleOrThrow(Integer roleId) {
        return roleRepo.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role", roleId));
    }

    private void checkEmailUniquenessOrThrow(String email) {
        if (userRepo.existsByEmail(email))
            throw new EmailAlreadyExistsException(email);
    }

    private void checkPasswordOrThrow(String rawPassword, String hashedPassword) {
        if (!passwordEncoder.matches(rawPassword, hashedPassword)) {
            throw new UnauthorizedException("Invalid email or password");
        }
    }

    private void checkDeletePermission(Long id, Long currentUserId) {
        if (!fetchUserOrThrow(currentUserId).getRole().getCode().equals("ADMIN") && !id.equals(currentUserId))
            throw new AccessDeniedException("You don't have enough authorities to delete this user");
    }
}
