package com.alikh.bookswap.controller;

import com.alikh.bookswap.config.JwtConfig;
import com.alikh.bookswap.dto.auth.request.ChangePasswordRequest;
import com.alikh.bookswap.dto.auth.request.LoginRequest;
import com.alikh.bookswap.dto.auth.request.RegisterRequest;
import com.alikh.bookswap.dto.auth.response.LoginResponse;
import com.alikh.bookswap.dto.auth.response.RefreshResponse;
import com.alikh.bookswap.dto.auth.response.RegisterResponse;
import com.alikh.bookswap.exception.UnauthorizedException;
import com.alikh.bookswap.service.AuthService;
import com.alikh.bookswap.service.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final JwtConfig jwtConfig;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest body
    ) {
        var response = service.register(body);
        var location = URI.create("/api/users/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest body,
            HttpServletResponse response
    ) {
        var result = service.login(body);
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
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UnauthorizedException("Refresh token is missing");
        }

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
            HttpServletResponse response
    ) {
        clearRefreshToken(response);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
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
