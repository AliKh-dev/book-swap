package com.alikh.bookswap.controller;

import com.alikh.bookswap.config.JwtConfig;
import com.alikh.bookswap.dto.auth.request.ChangePasswordRequest;
import com.alikh.bookswap.dto.auth.request.LoginRequest;
import com.alikh.bookswap.dto.auth.request.RegisterRequest;
import com.alikh.bookswap.dto.auth.response.LoginResponse;
import com.alikh.bookswap.dto.auth.response.RefreshResponse;
import com.alikh.bookswap.dto.auth.response.RegisterResponse;
import com.alikh.bookswap.service.AuthService;
import com.alikh.bookswap.service.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final JwtConfig jwtConfig;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            UriComponentsBuilder uriBuilder,
            @Valid @RequestBody RegisterRequest request) {
        var response = service.register(request);
        var uri = uriBuilder.path("/api/users/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        var result = service.login(request);
        response.addCookie(buildRefreshTokenCookie(result.refreshToken()));
        return ResponseEntity.ok(
                new LoginResponse(
                        result.userId(),
                        result.email(),
                        result.name(),
                        result.accessToken(),
                        null
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response) {

        var result = service.refreshAccessToken(refreshToken);

        response.addCookie(buildRefreshTokenCookie(result.newRefreshToken()));

        return ResponseEntity.ok(
                new RefreshResponse(
                        result.email(),
                        result.newAccessToken(),
                        null
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal Jwt jwt,
            HttpServletResponse response) {
        clearRefreshToken(response);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ChangePasswordRequest request) {

        service.changePassword(jwt, request);
        return ResponseEntity.noContent().build();
    }

    private Cookie buildRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        return cookie;
    }

    private void clearRefreshToken(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
