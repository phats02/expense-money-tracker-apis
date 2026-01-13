package com.ferb.expenseMoneyTracker.provider;

import com.ferb.expenseMoneyTracker.dto.CustomUserDetail;
import com.ferb.expenseMoneyTracker.dto.JwtSubject;
import com.ferb.expenseMoneyTracker.properties.CustomProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@DependsOn("customProperties")
public class JwtAccessTokenProvider extends JwtTokenProvider<String> {
    private static final long JWT_EXPIRATION_MS = 3_600_000L; // 1 hour


    public JwtAccessTokenProvider() {
        super(JWT_EXPIRATION_MS, CustomProperties.getJwtSecret());
    }

    public String generateToken(CustomUserDetail customUserDetail) {
        return super.generateToken(customUserDetail.getUsername());
    }

    public String generateToken(String userName) {
        return super.generateToken(userName);
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(this.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return JwtSubject.fromString(claims.getSubject(), String.class).getData();
    }
}
