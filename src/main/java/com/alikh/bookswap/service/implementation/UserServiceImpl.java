package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.user.request.UserCreateRequest;
import com.alikh.bookswap.dto.user.request.UserPatchRequest;
import com.alikh.bookswap.dto.user.request.UserUpdateRequest;
import com.alikh.bookswap.dto.user.response.UserDetailResponse;
import com.alikh.bookswap.dto.user.response.UserSummaryResponse;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.Role;
import com.alikh.bookswap.exception.EmailAlreadyExistsException;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.mapper.UserMapper;
import com.alikh.bookswap.repository.RoleRepository;
import com.alikh.bookswap.repository.UserRepository;
import com.alikh.bookswap.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserSummaryResponse create(UserCreateRequest dto) {
        boolean existence = userRepo.existsByEmail(dto.email());
        if (existence) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        Role role = roleRepo.findById(dto.roleId())
                .orElseThrow(() -> new NotFoundException("Role", dto.roleId()));

        AppUser entity = mapper.fromCreate(dto, role, passwordEncoder.encode(dto.password()));
        userRepo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailResponse get(Long id) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        if (!user.getIsDeleted())
            throw new NotFoundException("User", id);

        return mapper.toDetail(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryResponse> list() {
        return userRepo.findAll().stream()
                .filter(user -> user.getIsDeleted() == false)
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public void update(Long id, UserUpdateRequest dto) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));

        boolean existence = userRepo.existsByEmail(dto.email());
        if (existence) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        Role role = roleRepo.findById(dto.roleId())
                .orElseThrow(() -> new NotFoundException("User", id));

        mapper.applyUpdate(user, dto, role);
    }

    @Override
    public void patch(Long id, UserPatchRequest dto) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));

        if (dto.email() != null) {
            boolean existence = userRepo.existsByEmail(dto.email());
            if (existence) {
                throw new EmailAlreadyExistsException(dto.email());
            }
        }

        Role role = null;
        if (dto.roleId() != null) {
            role = roleRepo.findById(dto.roleId())
                    .orElseThrow(() -> new NotFoundException("Role", dto.roleId()));
        }

        mapper.applyPatch(user, dto, role);
    }

    @Override
    public void softDelete(Long id, String deleter) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));

        user.setIsDeleted(true);
        user.setDeletedBy(deleter);
        user.setDeletedAt(LocalDateTime.now());
    }

    @Override
    public void hardDeleted(Long id) {
        if (!userRepo.existsById(id)) {
            throw new NotFoundException("User", id);
        }
        userRepo.deleteById(id);
    }
}
