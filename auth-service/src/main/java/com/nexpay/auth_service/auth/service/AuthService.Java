package com.nexpay.auth_service.auth.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexpay.auth_service.auth.dto.AuthResponse;
import com.nexpay.auth_service.auth.dto.ChangePasswordRequest;
import com.nexpay.auth_service.auth.dto.ForgotPasswordRequest;
import com.nexpay.auth_service.auth.dto.LoginRequest;
import com.nexpay.auth_service.auth.dto.MessageResponse;
import com.nexpay.auth_service.auth.dto.RegisterRequest;
import com.nexpay.auth_service.auth.dto.ResetPasswordRequest;
import com.nexpay.auth_service.user.mapper.UserMapper;
import com.nexpay.auth_service.user.model.User;
import com.nexpay.auth_service.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements InterfaceAuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByCpf(request.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        String encodedPassword =
                passwordEncoder.encode(request.password());

        User user =
                userMapper.toEntity(request, encodedPassword);

        userRepository.save(user);

        return new AuthResponse(
                null,
                "Bearer"
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public MessageResponse changePassword(
            UUID userId,
            ChangePasswordRequest request) {

        throw new UnsupportedOperationException(
                "Unimplemented method 'changePassword'");
    }

    @Override
    public MessageResponse requestPasswordRecovery(
            ForgotPasswordRequest request) {

        throw new UnsupportedOperationException(
                "Unimplemented method 'requestPasswordRecovery'");
    }

    @Override
    public MessageResponse resetPassword(
            ResetPasswordRequest request) {

        throw new UnsupportedOperationException(
                "Unimplemented method 'resetPassword'");
    }
}