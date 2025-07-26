package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.user.request.UserCreateRequest;
import com.alikh.bookswap.dto.user.request.UserPatchRequest;
import com.alikh.bookswap.dto.user.request.UserUpdateRequest;
import com.alikh.bookswap.dto.user.response.UserDetailResponse;
import com.alikh.bookswap.dto.user.response.UserSummaryResponse;
import com.alikh.bookswap.service.contract.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<UserSummaryResponse> create(
            UriComponentsBuilder uriBuilder,
            @RequestBody @Valid UserCreateRequest dto) {
        var user = service.create(dto);
        var uri = uriBuilder.path("api/books/{id}").buildAndExpand(user.id()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                       @RequestBody @Valid UserUpdateRequest dto) {
        service.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patch(@PathVariable Long id,
                      @RequestBody @Valid UserPatchRequest dto) {
        service.patch(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        // TODO: probably should be change logic of getting name from SecurityContextHolder
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        service.softDelete(id, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        service.hardDeleted(id);
        return ResponseEntity.noContent().build();
    }
}
