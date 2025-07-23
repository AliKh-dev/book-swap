package com.alikh.bookswap.service;

import com.alikh.bookswap.config.JwtConfig;
import com.alikh.bookswap.entity.AppUser;
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

    public Jwt parse(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, config.getSecretKey());
        } catch (JwtException exception){
            return null;
        }
    }

    private Jwt generateToken(AppUser user, long tokenExpiration) {
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole().getCode())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();

        return new Jwt(claims, config.getSecretKey());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(config.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
