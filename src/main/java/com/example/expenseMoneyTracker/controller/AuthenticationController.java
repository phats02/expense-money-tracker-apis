package com.example.expenseMoneyTracker.controller;

import com.example.expenseMoneyTracker.dto.CustomUserDetail;
import com.example.expenseMoneyTracker.dto.LoginRequest;
import com.example.expenseMoneyTracker.dto.LoginResponse;
import com.example.expenseMoneyTracker.dto.SuccessResponse;
import com.example.expenseMoneyTracker.provider.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
@Tag(name="Auth service")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/login/password")
    public SuccessResponse<LoginResponse> loginWithUsernamePassword(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal());
        return new SuccessResponse<>(new LoginResponse(token));
    }

}
