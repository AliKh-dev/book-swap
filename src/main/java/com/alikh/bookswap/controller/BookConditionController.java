package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.bookcondition.request.BookConditionCreateRequest;
import com.alikh.bookswap.dto.bookcondition.request.BookConditionPatchRequest;
import com.alikh.bookswap.dto.bookcondition.request.BookConditionUpdateRequest;
import com.alikh.bookswap.dto.bookcondition.response.BookConditionDetailResponse;
import com.alikh.bookswap.dto.bookcondition.response.BookConditionSummaryResponse;
import com.alikh.bookswap.service.contract.BookConditionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/book-conditions")
@RequiredArgsConstructor
public class BookConditionController {

    private final BookConditionService service;

    @PostMapping
    public ResponseEntity<BookConditionSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid BookConditionCreateRequest request
    ) {
        var response = service.create(request);
        var uri = uriBuilder.path("/api/book-conditions/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BookConditionSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookConditionDetailResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookConditionSummaryResponse> update(
            @PathVariable Integer id,
            @RequestBody @Valid BookConditionUpdateRequest request
    ) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookConditionSummaryResponse> patch(
            @PathVariable Integer id,
            @RequestBody BookConditionPatchRequest request
    ) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
