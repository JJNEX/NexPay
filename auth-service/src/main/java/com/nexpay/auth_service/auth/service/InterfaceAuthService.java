package com.nexpay.auth_service.auth.service;

import com.nexpay.auth_service.auth.dto.AuthResponse;
import com.nexpay.auth_service.auth.dto.ChangePasswordRequest;
import com.nexpay.auth_service.auth.dto.ForgotPasswordRequest;
import com.nexpay.auth_service.auth.dto.LoginRequest;
import com.nexpay.auth_service.auth.dto.RegisterRequest;
import com.nexpay.auth_service.auth.dto.ResetPasswordRequest;
import com.nexpay.auth_service.auth.dto.MessageResponse;

import java.util.UUID;

public interface InterfaceAuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    MessageResponse changePassword(
            UUID userId,
            ChangePasswordRequest request);

    MessageResponse requestPasswordRecovery(
            ForgotPasswordRequest request);

    MessageResponse resetPassword(
            ResetPasswordRequest request);
}
