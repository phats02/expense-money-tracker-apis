package com.ferb.expenseMoneyTracker.service;

import com.ferb.expenseMoneyTracker.dto.LoginResponse;
import com.ferb.expenseMoneyTracker.entity.UsedRefreshToken;
import com.ferb.expenseMoneyTracker.provider.JwtAccessTokenProvider;
import com.ferb.expenseMoneyTracker.provider.JwtRefreshTokenProvider;
import com.ferb.expenseMoneyTracker.repository.UsedRefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private JwtAccessTokenProvider jwtAccessTokenProvider;
    @Autowired
    private JwtRefreshTokenProvider jwtRefreshTokenProvider;
    @Autowired
    private UsedRefreshTokenRepository usedRefreshTokenRepository;

    public LoginResponse refreshToken(String accessToken, String refreshToken) throws AccessDeniedException {
        if (!jwtRefreshTokenProvider.isTokenPairValid(accessToken, refreshToken)) {
            throw new AccessDeniedException("Token pair is invalid");
        }

        UUID accessTokenId = jwtRefreshTokenProvider.getAccessTokenId(refreshToken);

        if (usedRefreshTokenRepository.findById(accessTokenId).isPresent()) {
            throw new AccessDeniedException("Token pair has already used");
        }
        UsedRefreshToken refreshTokenRecord = UsedRefreshToken.builder()
                .id(accessTokenId)
                .expiredAt(jwtRefreshTokenProvider.getTokenExpiration(refreshToken))
                .build();
        usedRefreshTokenRepository.save(refreshTokenRecord);

        String username = jwtAccessTokenProvider.getUserIdFromJWT(accessToken);
        String newAccessToken = jwtAccessTokenProvider.generateToken(username);
        String newRefreshToken = jwtRefreshTokenProvider.generateToken(newAccessToken);

        return new LoginResponse(newAccessToken, newRefreshToken);
    }
}
