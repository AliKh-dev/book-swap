package com.alikh.bookswap.service;

import com.alikh.bookswap.dto.auth.request.ChangePasswordRequest;
import com.alikh.bookswap.dto.auth.request.LoginRequest;
import com.alikh.bookswap.dto.auth.request.RegisterRequest;
import com.alikh.bookswap.dto.auth.response.LoginResponse;
import com.alikh.bookswap.dto.auth.response.RefreshResponse;
import com.alikh.bookswap.dto.auth.response.RegisterResponse;
import com.alikh.bookswap.dto.user.request.UserChangePasswordRequest;
import com.alikh.bookswap.exception.InvalidTokenException;
import com.alikh.bookswap.exception.TokenExpiredException;
import com.alikh.bookswap.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {
        var user = request.toUserCreateRequest();
        return RegisterResponse.from(userService.create(user));
    }

    public LoginResponse login(LoginRequest request) {
        var user = userService.authenticate(request.toUserLoginRequest());

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                accessToken.asToken(),
                refreshToken.asToken()
        );
    }

    public RefreshResponse refreshAccessToken(String token) {
        Jwt jwt = jwtService.parse(token);
        if (jwt.isExpired())
            throw new TokenExpiredException();

        var user = userService.fetch(jwt.getUserId());

        Jwt accessToken = jwtService.generateAccessToken(user);
        Jwt refreshToken = jwtService.generateRefreshToken(user);

        return new RefreshResponse(
                user.getEmail(),
                accessToken.asToken(),
                refreshToken.asToken()
        );
    }

    public void changePassword(Jwt jwt, ChangePasswordRequest request) {
        userService.changePassword(
                new UserChangePasswordRequest(
                        jwt.getUserId(),
                        request.currentPassword(),
                        request.newPassword()
                )
        );
    }
}
