package com.nexpay.auth_service.user.mapper;

import org.springframework.stereotype.Component;

import com.nexpay.auth_service.auth.dto.RegisterRequest;
import com.nexpay.auth_service.user.dto.UserResponse;
import com.nexpay.auth_service.user.model.Role;
import com.nexpay.auth_service.user.model.User;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {

        User user = new User();

        user.setName(request.name());
        user.setCpf(request.cpf());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(Role.CUSTOMER);
        user.setActive(true);

        return user;
    }

    public UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail(),
                user.getActive(),
                user.getLastLogin(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getRole()
        );
    }
}