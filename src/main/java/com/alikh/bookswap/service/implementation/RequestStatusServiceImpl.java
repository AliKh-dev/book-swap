package com.alikh.bookswap.service.implementation;

import com.alikh.bookswap.dto.requeststatus.request.*;
import com.alikh.bookswap.dto.requeststatus.response.*;
import com.alikh.bookswap.entity.RequestStatus;
import com.alikh.bookswap.exception.CodeAlreadyExistsException;
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
        checkCodeUniquenessOrThrow(dto.code());
        var entity = mapper.fromCreate(dto, nextId);
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
        return mapper.toDetail(fetchRequestStatusOrThrow(id));
    }

    @Override
    public RequestStatusSummaryResponse update(Integer id, RequestStatusUpdateRequest dto) {
        var entity = fetchRequestStatusOrThrow(id);
        checkCodeUniquenessOrThrow(dto.code());
        mapper.applyUpdate(entity, dto);
        return mapper.toSummary(entity);
    }

    @Override
    public RequestStatusSummaryResponse patch(Integer id, RequestStatusPatchRequest dto) {
        var entity = fetchRequestStatusOrThrow(id);
        if (dto.code() != null)
            checkCodeUniquenessOrThrow(dto.code());
        mapper.applyPatch(entity, dto);
        return mapper.toSummary(entity);
    }

    @Override
    public void delete(Integer id) {
        fetchRequestStatusOrThrow(id);
        repo.deleteById(id);
    }

    private RequestStatus fetchRequestStatusOrThrow(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("RequestStatus", id));
    }

    private void checkCodeUniquenessOrThrow(String dto) {
        if (repo.existsByCode(dto))
            throw new CodeAlreadyExistsException("RequestStatus", dto);
    }
}
