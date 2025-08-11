package com.alikh.bookswap.service;

import com.alikh.bookswap.config.JwtConfig;
import com.alikh.bookswap.entity.AppUser;
import com.alikh.bookswap.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final JwtConfig config;
    private final SecretKey secretKey;

    public JwtService(JwtConfig config) {
        this.config = config;
        this.secretKey = config.getSecretKey();
    }

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

        return new Jwt(claims, secretKey);
    }

    public Jwt parse(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, secretKey);
        } catch (JwtException exception) {
            throw new InvalidTokenException();
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
