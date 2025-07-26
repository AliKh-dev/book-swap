package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.listing.request.ListingCreateRequest;
import com.alikh.bookswap.dto.listing.request.ListingPatchRequest;
import com.alikh.bookswap.dto.listing.request.ListingUpdateRequest;
import com.alikh.bookswap.dto.listing.response.ListingDetailResponse;
import com.alikh.bookswap.dto.listing.response.ListingSummaryResponse;
import com.alikh.bookswap.service.Jwt;
import com.alikh.bookswap.service.contract.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService service;

    @PostMapping
    public ResponseEntity<ListingSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid ListingCreateRequest request
    ) {
        var response = service.create(request);
        var uri = uriBuilder.path("/api/listings/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ListingSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingDetailResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingSummaryResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ListingUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.update(id, request, jwt.getUserId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ListingSummaryResponse> patch(
            @PathVariable Long id,
            @RequestBody ListingPatchRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.patch(id, request, jwt.getUserId()));
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
