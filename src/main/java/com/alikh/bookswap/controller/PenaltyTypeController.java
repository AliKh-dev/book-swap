package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.penaltytype.response.*;
import com.alikh.bookswap.dto.penaltytype.request.*;
import com.alikh.bookswap.service.contract.PenaltyTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/penalty-types")
@RequiredArgsConstructor
public class PenaltyTypeController {

    private final PenaltyTypeService service;

    @PostMapping
    public ResponseEntity<PenaltyTypeSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid PenaltyTypeCreateRequest request
    ) {
        var response = service.create(request);
        var uri = uriBuilder.path("/api/penalty-types/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PenaltyTypeSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PenaltyTypeDetailResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PenaltyTypeSummaryResponse> update(
            @PathVariable Integer id,
            @RequestBody @Valid PenaltyTypeUpdateRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PenaltyTypeSummaryResponse> patch(
            @PathVariable Integer id,
            @RequestBody @Valid PenaltyTypePatchRequest request
    ) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
