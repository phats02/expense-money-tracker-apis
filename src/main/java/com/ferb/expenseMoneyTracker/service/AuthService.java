package com.ferb.expenseMoneyTracker.service;

import com.ferb.expenseMoneyTracker.dto.LoginResponse;
import com.ferb.expenseMoneyTracker.provider.JwtAccessTokenProvider;
import com.ferb.expenseMoneyTracker.provider.JwtRefreshTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class AuthService {
    @Autowired
    private JwtAccessTokenProvider jwtAccessTokenProvider;
    @Autowired
    private JwtRefreshTokenProvider jwtRefreshTokenProvider;

    public LoginResponse refreshToken(String accessToken, String refreshToken) throws AccessDeniedException {
        if (!jwtRefreshTokenProvider.isTokenPairValid(accessToken, refreshToken)) {
            throw new AccessDeniedException("Token pair is invalid");
        }
        String username = jwtAccessTokenProvider.getUserIdFromJWT(accessToken);

        String newAccessToken = jwtAccessTokenProvider.generateToken(username);
        String newRefreshToken = jwtRefreshTokenProvider.generateToken(newAccessToken);
        return new LoginResponse(newAccessToken, newRefreshToken);
    }
}
