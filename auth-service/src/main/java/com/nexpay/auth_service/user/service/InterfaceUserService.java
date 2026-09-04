package com.nexpay.auth_service.user.service;

import java.util.List;
import java.util.UUID;

import com.nexpay.auth_service.user.dto.UpdateProfileRequest;
import com.nexpay.auth_service.user.dto.UserResponse;

public interface InterfaceUserService {

    List<UserResponse> findAll();

    UserResponse findById(UUID id);

    UserResponse activate(UUID id);

    UserResponse deactivate(UUID id);

    UserResponse update(
            UUID id,
            UpdateProfileRequest request);
}