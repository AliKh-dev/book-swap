package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.penalty.request.*;
import com.alikh.bookswap.dto.penalty.response.*;
import com.alikh.bookswap.service.contract.PenaltyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/penalties")
@RequiredArgsConstructor
public class PenaltyController {

    private final PenaltyService service;

    @PostMapping
    public ResponseEntity<PenaltySummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid PenaltyCreateRequest dto) {
        var Penalty = service.create(dto);
        var uri = uriBuilder.path("api/penalties/{id}").buildAndExpand(Penalty.id()).toUri();

        return ResponseEntity.created(uri).body(Penalty);
    }

    @GetMapping
    public List<PenaltySummaryResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public PenaltyDetailResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestBody @Valid PenaltyUpdateRequest dto) {
        service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable Long id,
                      @RequestBody @Valid PenaltyPatchRequest dto) {
        service.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
