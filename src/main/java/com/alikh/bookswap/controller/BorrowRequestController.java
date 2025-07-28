package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.bookcondition.request.BookConditionCreateRequest;
import com.alikh.bookswap.dto.bookcondition.request.BookConditionPatchRequest;
import com.alikh.bookswap.dto.bookcondition.request.BookConditionUpdateRequest;
import com.alikh.bookswap.dto.bookcondition.response.BookConditionDetailResponse;
import com.alikh.bookswap.dto.bookcondition.response.BookConditionSummaryResponse;
import com.alikh.bookswap.dto.borrowrequest.request.BorrowRequestCreateRequest;
import com.alikh.bookswap.dto.borrowrequest.request.BorrowRequestPatchRequest;
import com.alikh.bookswap.dto.borrowrequest.request.BorrowRequestUpdateRequest;
import com.alikh.bookswap.dto.borrowrequest.response.BorrowRequestDetailResponse;
import com.alikh.bookswap.dto.borrowrequest.response.BorrowRequestSummaryResponse;
import com.alikh.bookswap.service.Jwt;
import com.alikh.bookswap.service.contract.BookConditionService;
import com.alikh.bookswap.service.contract.BorrowRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/borrow-requests")
@RequiredArgsConstructor
public class BorrowRequestController {

    private final BorrowRequestService service;

    @PostMapping
    public ResponseEntity<BorrowRequestSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid BorrowRequestCreateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var response = service.create(request, jwt.getUserId());
        var uri = uriBuilder.path("/api/borrow-requests/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BorrowRequestSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping
    public ResponseEntity<List<BorrowRequestSummaryResponse>> list(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.list(jwt.getUserId()));
    }

    @GetMapping("/me/borrow-requests")
    public ResponseEntity<List<BorrowRequestSummaryResponse>> listRelatedToOwner(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.listRelatedToOwner(jwt.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRequestDetailResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowRequestSummaryResponse> update(
            @PathVariable Long id,
            @RequestBody @Validated BorrowRequestUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.update(id, jwt.getUserId(), request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BorrowRequestSummaryResponse> patch(
            @PathVariable Long id,
            @RequestBody BorrowRequestPatchRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.patch(id, jwt.getUserId(), request));
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
