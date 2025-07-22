package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.book.request.*;
import com.alikh.bookswap.dto.book.response.*;
import com.alikh.bookswap.service.contract.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            @RequestBody @Valid BookCreateRequest dto) {
        var book = service.create(dto);
        var uri = uriBuilder.path("api/books/{id}").buildAndExpand(book.id()).toUri();

        return ResponseEntity.created(uri).body(book);
    }

    @GetMapping
    public List<BookSummaryResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public BookDetailResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestBody @Valid BookUpdateRequest dto) {
        service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable Long id,
                      @RequestBody @Valid BookPatchRequest dto) {
        service.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
