package com.alikh.bookswap.controller;

import com.alikh.bookswap.dto.user.request.UserCreateRequest;
import com.alikh.bookswap.dto.user.request.UserPatchRequest;
import com.alikh.bookswap.dto.user.request.UserRoleChangeRequest;
import com.alikh.bookswap.dto.user.request.UserUpdateRequest;
import com.alikh.bookswap.dto.user.response.UserDetailResponse;
import com.alikh.bookswap.dto.user.response.UserSummaryResponse;
import com.alikh.bookswap.service.Jwt;
import com.alikh.bookswap.service.contract.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSummaryResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserSummaryResponse> patch(
            @PathVariable Long id,
            @RequestBody @Valid UserPatchRequest dto
    ) {
        return ResponseEntity.ok(service.patch(id, dto));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserSummaryResponse> changeRole(
            @PathVariable Long id,
            @RequestBody @Valid UserRoleChangeRequest dto
    ) {
        return ResponseEntity.ok(service.changeRole(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam(name = "hard",required = false) boolean hard,
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (hard)
            service.hardDeleted(id, jwt.getUserId());
        else
            service.softDelete(id, jwt.getUserId());
        return ResponseEntity.noContent().build();
    }
}
