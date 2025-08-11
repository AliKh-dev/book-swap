package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.role.request.*;
import com.alikh.bookswap.dto.role.response.*;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.entity.Role;
import com.alikh.bookswap.exception.DuplicateCodeException;
import com.alikh.bookswap.exception.parents.NotFoundException;
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
        checkCodeUniquenessOrThrow(dto.code());
        var entity = mapper.fromCreate(dto, nextId);
        roleRepo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleSummaryResponse> list() {
        return roleRepo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public Role get(Integer id) {
        return roleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id=" + id + "not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDetailResponse get(Integer id, int page) {
        var role = get(id);

        Page<AppUser> users = userRepo.findByRole(
                role, PageRequest.of(page, PAGE_SIZE, Sort.by("id").ascending()));

        return mapper.toDetail(role, users.getContent());
    }

    @Override
    public RoleSummaryResponse update(Integer id, RoleUpdateRequest dto) {
        var entity = get(id);
        checkCodeUniquenessOrThrow(dto.code());
        mapper.applyUpdate(entity, dto);
        return mapper.toSummary(entity);
    }

    @Override
    public RoleSummaryResponse patch(Integer id, RolePatchRequest dto) {
        var entity = get(id);
        if (dto.code() != null)
            checkCodeUniquenessOrThrow(dto.code());
        mapper.applyPatch(entity, dto);
        return mapper.toSummary(entity);
    }

    @Override
    public void delete(Integer id) {
        get(id);
        roleRepo.deleteById(id);
    }

    private void checkCodeUniquenessOrThrow(String dto) {
        if (roleRepo.findByCode(dto).isPresent())
            throw new DuplicateCodeException("Role", dto);
    }
}
