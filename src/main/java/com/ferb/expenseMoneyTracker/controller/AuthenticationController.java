package com.ferb.expenseMoneyTracker.controller;

import com.ferb.expenseMoneyTracker.annotations.ToLowerCase;
import com.ferb.expenseMoneyTracker.dto.*;
import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.enums.SignUpMethod;
import com.ferb.expenseMoneyTracker.exception.FieldAlreadyExisted;
import com.ferb.expenseMoneyTracker.provider.JwtAccessTokenProvider;
import com.ferb.expenseMoneyTracker.provider.JwtRefreshTokenProvider;
import com.ferb.expenseMoneyTracker.service.AuthService;
import com.ferb.expenseMoneyTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name="Auth service")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtAccessTokenProvider jwtAccessTokenProvider;
    @Autowired
    private JwtRefreshTokenProvider jwtRefreshTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @Operation(summary = "Login with email and password")
    @PostMapping("/login/password")
    public SuccessResponse<LoginResponse> loginWithUsernamePassword(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtAccessTokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal());
        String refreshToken = jwtRefreshTokenProvider.generateToken(accessToken);
        return new SuccessResponse<>(new LoginResponse(accessToken, refreshToken));
    }

    @Operation(summary = "Signup with email and password")
    @PostMapping("/signup/password")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<LoginResponse> signupWithEmailPassword(@Valid @RequestBody SignupByPasswordRequest signupByPasswordRequest) {
        if (userService.isEmailExist(signupByPasswordRequest.getEmail())) {
            throw new FieldAlreadyExisted("Email");
        }

        User newUserInfo = User.builder()
                .email(signupByPasswordRequest.getEmail())
                .password(passwordEncoder.encode(signupByPasswordRequest.getPassword()))
                .timezone(signupByPasswordRequest.getTimezone())
                .signUpMethod(SignUpMethod.password)
                .build();

        User insertedUser = userService.saveUser(newUserInfo);


        String accessToken = jwtAccessTokenProvider.generateToken(new CustomUserDetail(insertedUser));
        String refreshToken = jwtRefreshTokenProvider.generateToken(accessToken);

        return new SuccessResponse<>(new LoginResponse(accessToken, refreshToken));
    }

    @Operation(summary = "Check is email available to signup")
    @GetMapping("/isEmailAvailable")
    public SuccessResponse<Boolean> isEmailAvailable( @Valid @RequestParam(name = "email", required = true) @Email @ToLowerCase String email) {
        return new SuccessResponse<Boolean>(!userService.isEmailExist(email));
    }

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh")
    public SuccessResponse<LoginResponse> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) throws AccessDeniedException {
        return new SuccessResponse<>(authService.refreshToken(refreshTokenRequest.getAccessToken(), refreshTokenRequest.getRefreshToken()));
    }

}
