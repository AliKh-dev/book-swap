package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.role.request.*;
import com.alikh.bookswap.dto.role.response.*;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.Role;
import com.alikh.bookswap.exception.CodeAlreadyExistsException;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.mapper.RoleMapper;
import com.alikh.bookswap.repository.UserRepository;
import com.alikh.bookswap.repository.RoleRepository;
import com.alikh.bookswap.service.contract.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final RoleMapper mapper;

    private static final int PAGE_SIZE = 5;

    @Override
    public RoleSummaryResponse create(RoleCreateRequest dto) {
        Integer nextId = roleRepo.findTopByOrderByIdDesc()
                .map(Role::getId).orElse(0) + 1;

        checkCodeDuplication(dto.code());

        Role role = mapper.fromCreate(dto, nextId);
        roleRepo.save(role);
        return mapper.toSummary(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDetailResponse get(Integer id, int page) {
        Role role = roleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Role", id));

        Page<AppUser> users = userRepo.findByRole(
                role, PageRequest.of(page, PAGE_SIZE, Sort.by("id").ascending()));

        return mapper.toDetail(role, users.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleSummaryResponse> list() {
        return roleRepo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public void update(Integer id, RoleUpdateRequest dto) {
        Role role = roleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Role", id));
        checkCodeDuplication(dto.code());
        mapper.applyUpdate(role, dto);
    }

    @Override
    public void patch(Integer id, RolePatchRequest dto) {
        Role role = roleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Role", id));
        if (dto.code() != null) checkCodeDuplication(dto.code());
        mapper.applyPatch(role, dto);
    }

    @Override
    public void delete(Integer id) {
        if (!roleRepo.existsById(id))
            throw new NotFoundException("Role", id);
        roleRepo.deleteById(id);
    }

    private void checkCodeDuplication(String dto) {
        if (roleRepo.findByCode(dto).isPresent())
            throw new CodeAlreadyExistsException("Role", dto);
    }
}
