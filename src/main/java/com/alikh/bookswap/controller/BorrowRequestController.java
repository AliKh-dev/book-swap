package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.borrowrequest.request.BorrowRequestPatchRequest;
import com.alikh.bookswap.dto.borrowrequest.request.BorrowRequestUpdateRequest;
import com.alikh.bookswap.dto.borrowrequest.response.BorrowRequestDetailResponse;
import com.alikh.bookswap.dto.borrowrequest.response.BorrowRequestSummaryResponse;
import com.alikh.bookswap.service.Jwt;
import com.alikh.bookswap.service.contract.BorrowRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BorrowRequestController {

    private final BorrowRequestService service;

    @PostMapping("/listings/{listingId}/borrow-requests")
    public ResponseEntity<BorrowRequestSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @PathVariable Long listingId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var response = service.create(listingId, jwt.getUserId());
        var uri = uriBuilder.path("/api/borrow-requests/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/me/borrow-requests")
    public ResponseEntity<List<BorrowRequestSummaryResponse>> listMine(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.listMine(jwt.getUserId()));
    }

    @GetMapping("/me/owned-borrow-requests")
    public ResponseEntity<List<BorrowRequestSummaryResponse>> listForMyBooks(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.listByOwner(jwt.getUserId()));
    }

    @GetMapping("/borrow-requests")
    public ResponseEntity<List<BorrowRequestSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/borrow-requests/{id}")
    public ResponseEntity<BorrowRequestDetailResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/borrow-requests/{id}")
    public ResponseEntity<BorrowRequestSummaryResponse> update(
            @PathVariable Long id,
            @RequestBody @Validated BorrowRequestUpdateRequest body,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.update(id, jwt.getUserId(), body));
    }

    @PatchMapping("/borrow-requests/{id}")
    public ResponseEntity<BorrowRequestSummaryResponse> patch(
            @PathVariable Long id,
            @RequestBody BorrowRequestPatchRequest body,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.patch(id, jwt.getUserId(), body));
    }

    @DeleteMapping("/borrow-requests/{id}")
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
