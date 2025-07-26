package com.alikh.bookswap.service;

import com.alikh.bookswap.config.JwtConfig;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtConfig config;

    public Jwt generateAccessToken(AppUser user) {
        return generateToken(user, config.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(AppUser user) {
        return generateToken(user, config.getRefreshTokenExpiration());
    }

    private Jwt generateToken(AppUser user, long tokenExpiration) {
        var now = new Date();
        var exp = new Date(now.getTime() + 1000 * tokenExpiration);

        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole().getCode())
                .issuedAt(now)
                .expiration(exp)
                .build();

        return new Jwt(claims, config.getSecretKey());
    }

    public Jwt parse(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, config.getSecretKey());
        } catch (JwtException exception) {
            throw new InvalidTokenException("Invalid JWT", exception);
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(config.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
