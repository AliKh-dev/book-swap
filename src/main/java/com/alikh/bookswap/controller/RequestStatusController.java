package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.requeststatus.request.RequestStatusCreateRequest;
import com.alikh.bookswap.dto.requeststatus.request.RequestStatusPatchRequest;
import com.alikh.bookswap.dto.requeststatus.request.RequestStatusUpdateRequest;
import com.alikh.bookswap.dto.requeststatus.response.RequestStatusDetailResponse;
import com.alikh.bookswap.dto.requeststatus.response.RequestStatusSummaryResponse;
import com.alikh.bookswap.service.contract.RequestStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/request-statuses")
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
    public ResponseEntity<RequestStatusSummaryResponse> update(
            @PathVariable Integer id,
            @RequestBody @Valid RequestStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RequestStatusSummaryResponse> patch(
            @PathVariable Integer id,
            @RequestBody @Valid RequestStatusPatchRequest request
    ) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
