package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.LoginRequest;
import com.wtmsbackend.dto.request.UserRequest;
import com.wtmsbackend.dto.response.LoginResponse;
import com.wtmsbackend.dto.response.UserResponse;
import com.wtmsbackend.services.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@SecurityRequirements()
@Slf4j
@Tag(name = "Auth", description = "")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest) {
        UserResponse user = authService.register(userRequest);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message("User registered successfully!")
                .success(true)
                .payload(user)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginData = authService.login(request);
        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .message("Login successfully!")
                .success(true)
                .payload(loginData)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}