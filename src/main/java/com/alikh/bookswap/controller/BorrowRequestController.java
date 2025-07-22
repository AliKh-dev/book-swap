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
import com.alikh.bookswap.service.contract.BookConditionService;
import com.alikh.bookswap.service.contract.BorrowRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            @RequestBody @Valid BorrowRequestCreateRequest request) {

        // TODO: I should take borrowerId from jwt token
//        var response = service.create(request, );
//        var uri = uriBuilder.path("/api/book-conditions/{id}").buildAndExpand(response.id()).toUri();
//        return ResponseEntity.created(uri).body(response);
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<BorrowRequestSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRequestDetailResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody @Validated BorrowRequestUpdateRequest request) {
        service.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patch(
            @PathVariable Long id,
            @RequestBody BorrowRequestPatchRequest request) {
        service.patch(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
