package com.nexpay.auth_service.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.nexpay.auth_service.user.model.Role;

public record UserResponse(

        UUID id,

        String name,

        String cpf,

        String email,

        boolean active,

        LocalDateTime lastLogin,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        Role role

) {
}