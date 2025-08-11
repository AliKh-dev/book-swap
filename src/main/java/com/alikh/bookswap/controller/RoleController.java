package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.role.request.RoleCreateRequest;
import com.alikh.bookswap.dto.role.request.RolePatchRequest;
import com.alikh.bookswap.dto.role.request.RoleUpdateRequest;
import com.alikh.bookswap.dto.role.response.RoleDetailResponse;
import com.alikh.bookswap.dto.role.response.RoleSummaryResponse;
import com.alikh.bookswap.service.contract.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @PostMapping
    public ResponseEntity<RoleSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid RoleCreateRequest request
    ) {
        var response = service.create(request);
        var uri = uriBuilder.path("/api/roles/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RoleSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDetailResponse> get(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") Integer page
    ) {
        return ResponseEntity.ok(service.get(id, page));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleSummaryResponse> update(
            @PathVariable Integer id,
            @RequestBody @Valid RoleUpdateRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoleSummaryResponse> patch(
            @PathVariable Integer id,
            @RequestBody @Valid RolePatchRequest request
    ) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
