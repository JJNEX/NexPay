package com.nexpay.auth_service.auth.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexpay.auth_service.auth.dto.AuthResponse;
import com.nexpay.auth_service.auth.dto.ChangePasswordRequest;
import com.nexpay.auth_service.auth.dto.ForgotPasswordRequest;
import com.nexpay.auth_service.auth.dto.LoginRequest;
import com.nexpay.auth_service.auth.dto.MessageResponse;
import com.nexpay.auth_service.auth.dto.RegisterRequest;
import com.nexpay.auth_service.auth.dto.ResetPasswordRequest;
import com.nexpay.auth_service.auth.service.InterfaceAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final InterfaceAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password/{userId}")
    public ResponseEntity<MessageResponse> changePassword(
            @PathVariable UUID userId,
            @RequestBody ChangePasswordRequest request) {

        MessageResponse response =
                authService.changePassword(userId, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> requestPasswordRecovery(
            @RequestBody ForgotPasswordRequest request) {

        MessageResponse response =
                authService.requestPasswordRecovery(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        MessageResponse response =
                authService.resetPassword(request);

        return ResponseEntity.ok(response);
    }
}