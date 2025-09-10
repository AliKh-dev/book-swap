package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.book.request.BookCreateRequest;
import com.alikh.bookswap.dto.book.request.BookPatchRequest;
import com.alikh.bookswap.dto.book.request.BookUpdateRequest;
import com.alikh.bookswap.dto.book.response.BookDetailResponse;
import com.alikh.bookswap.dto.book.response.BookSummaryResponse;
import com.alikh.bookswap.service.Jwt;
import com.alikh.bookswap.service.implementation.command.BookCommandService;
import com.alikh.bookswap.service.implementation.query.BookQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final BookQueryService queryService;
    private final BookCommandService commandService;

    @PostMapping("/books")
    public ResponseEntity<BookSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid BookCreateRequest dto
    ) {
        var response = commandService.create(dto, jwt.getUserId());
        var uri = uriBuilder.path("/api/books/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/me/books")
    public ResponseEntity<List<BookSummaryResponse>> listMine(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(queryService.listMine(jwt.getUserId()));
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookSummaryResponse>> list() {
        return ResponseEntity.ok(queryService.list());
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDetailResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.get(id));
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<BookSummaryResponse> update(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid BookUpdateRequest dto
    ) {
        return ResponseEntity.ok(commandService.update(id, dto, jwt.getUserId()));
    }

    @PatchMapping("/books/{id}")
    public ResponseEntity<BookSummaryResponse> patch(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid BookPatchRequest dto
    ) {
        return ResponseEntity.ok(commandService.patch(id, dto, jwt.getUserId()));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam(required = false) boolean hard,
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (hard)
            commandService.hardDelete(id, jwt.getUserId());
        else
            commandService.softDelete(id, jwt.getUserId());
        return ResponseEntity.noContent().build();
    }
}
