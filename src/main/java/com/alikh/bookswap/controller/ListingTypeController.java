package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.listingtype.request.ListingTypeCreateRequest;
import com.alikh.bookswap.dto.listingtype.request.ListingTypePatchRequest;
import com.alikh.bookswap.dto.listingtype.request.ListingTypeUpdateRequest;
import com.alikh.bookswap.dto.listingtype.response.ListingTypeDetailResponse;
import com.alikh.bookswap.dto.listingtype.response.ListingTypeSummaryResponse;
import com.alikh.bookswap.dto.role.request.RoleCreateRequest;
import com.alikh.bookswap.dto.role.request.RolePatchRequest;
import com.alikh.bookswap.dto.role.request.RoleUpdateRequest;
import com.alikh.bookswap.dto.role.response.RoleDetailResponse;
import com.alikh.bookswap.dto.role.response.RoleSummaryResponse;
import com.alikh.bookswap.service.contract.ListingTypeService;
import com.alikh.bookswap.service.contract.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/listing-types")
@RequiredArgsConstructor
public class ListingTypeController {

    private final ListingTypeService service;

    @PostMapping
    public ResponseEntity<ListingTypeSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid ListingTypeCreateRequest request
    ) {
        var response = service.create(request);
        var uri = uriBuilder.path("/api/listing-types/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ListingTypeSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingTypeDetailResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Integer id,
            @RequestBody @Valid ListingTypeUpdateRequest request
    ) {
        service.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patch(
            @PathVariable Integer id,
            @RequestBody @Valid ListingTypePatchRequest request
    ) {
        service.patch(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
