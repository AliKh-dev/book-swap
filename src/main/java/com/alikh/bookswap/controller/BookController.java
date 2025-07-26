package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.book.request.BookCreateRequest;
import com.alikh.bookswap.dto.book.request.BookPatchRequest;
import com.alikh.bookswap.dto.book.request.BookUpdateRequest;
import com.alikh.bookswap.dto.book.response.BookDetailResponse;
import com.alikh.bookswap.dto.book.response.BookSummaryResponse;
import com.alikh.bookswap.service.Jwt;
import com.alikh.bookswap.service.contract.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @PostMapping
    public ResponseEntity<BookSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid BookCreateRequest dto
    ) {
        var response = service.create(dto, jwt.getUserId());
        var uri = uriBuilder.path("api/books/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BookSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDetailResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookSummaryResponse> update(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid BookUpdateRequest dto
    ) {
        return ResponseEntity.ok(service.update(id, dto, jwt.getUserId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookSummaryResponse> patch(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid BookPatchRequest dto
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
