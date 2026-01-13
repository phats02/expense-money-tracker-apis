package com.ferb.expenseMoneyTracker.provider;

import com.ferb.expenseMoneyTracker.dto.JwtSubject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public abstract class JwtTokenProvider<T> {


    private final long expirationMS;
    private final String jwtSecret;



    public JwtTokenProvider(long expirationMS,  String jwtSecret) {
        this.expirationMS = expirationMS;

        this.jwtSecret = jwtSecret;
    }


    protected SecretKey getSigningKey() {
        byte[] keyBytes =jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(T subjectData) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMS);

        return Jwts.builder()
                .subject(String.valueOf(new JwtSubject<T>(subjectData)))
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public String getTokenId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return JwtSubject.fromString(claims.getSubject(), String.class).getId();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("Invalid JWT token: {}", ex.getMessage());
            return false;
        }
    }
}