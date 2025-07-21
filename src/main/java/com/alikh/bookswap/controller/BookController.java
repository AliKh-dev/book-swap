package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.bookcondition.response.*;
import com.alikh.bookswap.dto.bookcondition.request.*;
import com.alikh.bookswap.service.contract.BookConditionService;
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

    private final BookConditionService svc;

    @PostMapping
    public ResponseEntity<BookConditionSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid BookConditionCreateRequest dto) {
        var bookCondition = svc.create(dto);
        var uri = uriBuilder.path("/book-conditions/{id}").buildAndExpand(bookCondition.id()).toUri();

        return ResponseEntity.created(uri).body(bookCondition);
    }

    @GetMapping
    public List<BookConditionSummaryResponse> list() {
        return svc.list();
    }

    @GetMapping("/{id}")
    public BookConditionDetailResponse get(@PathVariable Integer id) {
        return svc.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Integer id,
                       @RequestBody @Valid BookConditionUpdateRequest dto) {
        svc.update(id, dto);
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable Integer id,
                      @RequestBody @Valid BookConditionPatchRequest dto) {
        svc.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        svc.delete(id);
    }
}
