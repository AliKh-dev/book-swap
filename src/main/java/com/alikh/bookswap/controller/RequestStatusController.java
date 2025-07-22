package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.requeststatus.request.RequestStatusCreateRequest;
import com.alikh.bookswap.dto.requeststatus.request.RequestStatusPatchRequest;
import com.alikh.bookswap.dto.requeststatus.request.RequestStatusUpdateRequest;
import com.alikh.bookswap.dto.requeststatus.response.RequestStatusDetailResponse;
import com.alikh.bookswap.dto.requeststatus.response.RequestStatusSummaryResponse;
import com.alikh.bookswap.dto.role.request.RoleCreateRequest;
import com.alikh.bookswap.dto.role.request.RolePatchRequest;
import com.alikh.bookswap.dto.role.request.RoleUpdateRequest;
import com.alikh.bookswap.dto.role.response.RoleDetailResponse;
import com.alikh.bookswap.dto.role.response.RoleSummaryResponse;
import com.alikh.bookswap.service.contract.RequestStatusService;
import com.alikh.bookswap.service.contract.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/request-statuses")
@RequiredArgsConstructor
public class RequestStatusController {

    private final RequestStatusService service;

    @PostMapping
    public ResponseEntity<RequestStatusSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid RequestStatusCreateRequest request
    ) {
        var response = service.create(request);
        var uri = uriBuilder.path("/api/request-statuses/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RequestStatusSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestStatusDetailResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Integer id,
            @RequestBody @Valid RequestStatusUpdateRequest request
    ) {
        service.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patch(
            @PathVariable Integer id,
            @RequestBody @Valid RequestStatusPatchRequest request
    ) {
        service.patch(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
