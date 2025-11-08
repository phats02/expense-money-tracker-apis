package com.example.expenseMoneyTracker.controller;

import com.example.expenseMoneyTracker.dto.CustomUserDetail;
import com.example.expenseMoneyTracker.dto.LoginRequest;
import com.example.expenseMoneyTracker.provider.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/login/password")
    public String loginWithUsernamePassword(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal());
    }

    @GetMapping("/protected")
    public String getProtected(@AuthenticationPrincipal UserDetails user) {
        return "It should be protected!";
    }
}
