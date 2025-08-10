package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.penalty.request.*;
import com.alikh.bookswap.dto.penalty.response.*;
import com.alikh.bookswap.service.Jwt;
import com.alikh.bookswap.service.contract.PenaltyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PenaltyController {

    private final PenaltyService service;

    @PostMapping("/borrow-requests/{reqId}/penalties")
    public ResponseEntity<PenaltySummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @PathVariable Long reqId,
            @RequestBody @Valid PenaltyCreateRequest body
    ) {
        var response = service.create(body, reqId);
        var uri = uriBuilder.path("api/penalties/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/penalties")
    public ResponseEntity<List<PenaltySummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/penalties/{id}")
    public ResponseEntity<PenaltyDetailResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/penalties/{id}")
    public ResponseEntity<PenaltySummaryResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid PenaltyUpdateRequest dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.update(id, dto, jwt.getUserId()));
    }

    @PatchMapping("/penalties/{id}")
    public ResponseEntity<PenaltySummaryResponse> patch(
            @PathVariable Long id,
            @RequestBody @Valid PenaltyPatchRequest dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.patch(id, dto, jwt.getUserId()));
    }

    @DeleteMapping("/penalties/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam(required = false) boolean hard,
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (hard)
            service.hardDelete(id, jwt.getUserId());
        else
            service.softDelete(id, jwt.getUserId());
        return ResponseEntity.noContent().build();
    }
}
