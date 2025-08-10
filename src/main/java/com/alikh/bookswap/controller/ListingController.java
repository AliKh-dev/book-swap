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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ListingController {

    private final ListingService service;

    @PostMapping("/books/{bookId}/listings")
    public ResponseEntity<ListingSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @PathVariable Long bookId,
            @RequestBody @Valid ListingCreateRequest body,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var response = service.create(body, bookId, jwt.getUserId());
        var uri = uriBuilder.path("/api/listings/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/me/listings")
    public ResponseEntity<List<ListingSummaryResponse>> listMine(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.listMine(jwt.getUserId()));
    }

    @GetMapping("/books/{bookId}/listings")
    public ResponseEntity<List<ListingSummaryResponse>> listByBook(
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(service.listByBook(bookId));
    }

    @GetMapping("/listings")
    public ResponseEntity<List<ListingSummaryResponse>> listAll() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/listings/{id}")
    public ResponseEntity<ListingDetailResponse> get(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/listings/{id}")
    public ResponseEntity<ListingSummaryResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ListingUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.update(id, request, jwt.getUserId()));
    }

    @PatchMapping("/listings/{id}")
    public ResponseEntity<ListingSummaryResponse> patch(
            @PathVariable Long id,
            @RequestBody ListingPatchRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.patch(id, request, jwt.getUserId()));
    }

    @DeleteMapping("/listings/{id}")
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
