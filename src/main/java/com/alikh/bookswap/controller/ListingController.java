package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.borrowrequest.request.BorrowRequestCreateRequest;
import com.alikh.bookswap.dto.borrowrequest.request.BorrowRequestPatchRequest;
import com.alikh.bookswap.dto.borrowrequest.request.BorrowRequestUpdateRequest;
import com.alikh.bookswap.dto.borrowrequest.response.BorrowRequestDetailResponse;
import com.alikh.bookswap.dto.borrowrequest.response.BorrowRequestSummaryResponse;
import com.alikh.bookswap.dto.listing.request.ListingCreateRequest;
import com.alikh.bookswap.dto.listing.request.ListingPatchRequest;
import com.alikh.bookswap.dto.listing.request.ListingUpdateRequest;
import com.alikh.bookswap.dto.listing.response.ListingDetailResponse;
import com.alikh.bookswap.dto.listing.response.ListingSummaryResponse;
import com.alikh.bookswap.service.contract.BorrowRequestService;
import com.alikh.bookswap.service.contract.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            @RequestBody @Valid ListingCreateRequest request) {

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
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody @Validated ListingUpdateRequest request) {
        service.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patch(
            @PathVariable Long id,
            @RequestBody ListingPatchRequest request) {
        service.patch(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
