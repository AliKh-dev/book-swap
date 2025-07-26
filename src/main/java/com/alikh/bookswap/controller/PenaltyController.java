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
@RequestMapping("/penalties")
@RequiredArgsConstructor
public class PenaltyController {

    private final PenaltyService service;

    @PostMapping
    public ResponseEntity<PenaltySummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid PenaltyCreateRequest dto
    ) {
        var response = service.create(dto);
        var uri = uriBuilder.path("api/penalties/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PenaltySummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PenaltyDetailResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    // TODO: for rest of methods admin privileges required
    @PutMapping("/{id}")
    public ResponseEntity<PenaltySummaryResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid PenaltyUpdateRequest dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.update(id, dto, jwt.getUserId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PenaltySummaryResponse> patch(
            @PathVariable Long id,
            @RequestBody @Valid PenaltyPatchRequest dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.patch(id, dto, jwt.getUserId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        service.softDelete(id, jwt.getUserId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<Void> hardDelete(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        service.hardDelete(id, jwt.getUserId());
        return ResponseEntity.noContent().build();
    }
}
