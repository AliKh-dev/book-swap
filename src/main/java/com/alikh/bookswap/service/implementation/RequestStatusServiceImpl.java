package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.requeststatus.request.*;
import com.alikh.bookswap.dto.requeststatus.response.*;
import com.alikh.bookswap.entity.RequestStatus;
import com.alikh.bookswap.exception.CodeAlreadyExists;
import com.alikh.bookswap.exception.NotFoundException;
import com.alikh.bookswap.mapper.RequestStatusMapper;
import com.alikh.bookswap.repository.RequestStatusRepository;
import com.alikh.bookswap.service.contract.RequestStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class RequestStatusServiceImpl implements RequestStatusService {
    private final RequestStatusRepository repo;

    private final RequestStatusMapper mapper;

    @Override
    public RequestStatusSummaryResponse create(RequestStatusCreateRequest dto) {
        Integer nextId = repo.findTopByOrderByIdDesc()
                .map(RequestStatus::getId)
                .orElse(0) + 1;
        checkCodeDuplication(dto.code());
        RequestStatus entity = mapper.fromCreate(dto, nextId);
        repo.save(entity);
        return mapper.toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestStatusSummaryResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RequestStatusDetailResponse get(Integer id) {
        return mapper.toDetail(repo.findById(id)
                .orElseThrow(() -> new NotFoundException("RequestStatus", id)));
    }

    @Override
    public void update(Integer id, RequestStatusUpdateRequest dto) {
        RequestStatus entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("RequestStatus", id));
        checkCodeDuplication(dto.code());
        mapper.applyUpdate(entity, dto);
    }

    @Override
    public void patch(Integer id, RequestStatusPatchRequest dto) {
        RequestStatus entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("RequestStatus", id));
        if (dto.code() != null) checkCodeDuplication(dto.code());
        mapper.applyPatch(entity, dto);
    }

    @Override
    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new NotFoundException("RequestStatus", id);
        repo.deleteById(id);
    }

    private void checkCodeDuplication(String dto) {
        if (repo.findByCode(dto).isPresent())
            throw new CodeAlreadyExists("RequestStatus", dto);
    }
}
