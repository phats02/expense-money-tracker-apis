package com.ferb.expenseMoneyTracker.provider;

import com.ferb.expenseMoneyTracker.dto.JwtSubject;
import com.ferb.expenseMoneyTracker.properties.CustomProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@DependsOn("customProperties")
public class JwtRefreshTokenProvider extends JwtTokenProvider<String>{

    private static final long JWT_EXPIRATION_MS = 604_800_000L; // 1 week
    @Autowired
    private JwtAccessTokenProvider jwtAccessTokenProvider;

    public JwtRefreshTokenProvider() {
        super(JWT_EXPIRATION_MS, CustomProperties.getJwtRefreshSecret());
    }

    public String generateToken(String accessToken) {
        UUID accessTokenId = jwtAccessTokenProvider.getTokenId(accessToken);

        return super.generateToken(accessTokenId.toString());
    }

    public UUID getAccessTokenId(String refreshToken) {
        Claims claims = Jwts.parser()
                .verifyWith(this.getSigningKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

        return JwtSubject.fromString(claims.getSubject(), UUID.class).getData();
    }

    public boolean isTokenPairValid(String accessToken, String refreshToken) {
        UUID accessTokenId = jwtAccessTokenProvider.getTokenId(accessToken);

        UUID accessTokenIdInRefreshToken = this.getAccessTokenId(refreshToken);

        return accessTokenId.equals(accessTokenIdInRefreshToken);
    }
}
