package com.clicKart.security.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationTime) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);

        this.expirationTime = expirationTime;
    }

    // Generate JWT
    public String generateToken(String username) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    // Common method to parse JWT and get claims
    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Get subject from JWT
    public String extractUsername(String token) {

        return getClaims(token)
                .getSubject();
    }

    // Check expiration
    public boolean isTokenExpired(String token) {

        Date expiration = getClaims(token)
                .getExpiration();

        return expiration.before(new Date());
    }

    // Validate JWT
    public boolean isTokenValid(String token) {

        try {


            return !isTokenExpired(token);

        } catch (Exception e) {

            return false;
        }
    }
}