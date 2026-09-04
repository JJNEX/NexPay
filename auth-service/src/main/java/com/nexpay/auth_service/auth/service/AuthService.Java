package com.nexpay.auth_service.auth.service;

import java.util.UUID;

import com.nexpay.auth_service.auth.dto.AuthResponse;
import com.nexpay.auth_service.auth.dto.ChangePasswordRequest;
import com.nexpay.auth_service.auth.dto.ForgotPasswordRequest;
import com.nexpay.auth_service.auth.dto.LoginRequest;
import com.nexpay.auth_service.auth.dto.MessageResponse;
import com.nexpay.auth_service.auth.dto.RegisterRequest;
import com.nexpay.auth_service.auth.dto.ResetPasswordRequest;

public class AuthService implements InterfaceAuthService{

    @Override
    public AuthResponse register(RegisterRequest request) {
        
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public MessageResponse changePassword(UUID userId, ChangePasswordRequest request) {
        
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public MessageResponse requestPasswordRecovery(ForgotPasswordRequest request) {
        
        throw new UnsupportedOperationException("Unimplemented method 'requestPasswordRecovery'");
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        
        throw new UnsupportedOperationException("Unimplemented method 'resetPassword'");
    }
    
}
