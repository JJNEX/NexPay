package com.nexpay.auth_service.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexpay.auth_service.user.dto.UpdateProfileRequest;
import com.nexpay.auth_service.user.dto.UserResponse;
import com.nexpay.auth_service.user.mapper.UserMapper;
import com.nexpay.auth_service.user.model.User;
import com.nexpay.auth_service.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements InterfaceUserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse activate(UUID id) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        user.activate();

        User saved = repository.save(user);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserResponse deactivate(UUID id) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        user.deactivate();

        User saved = repository.save(user);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserResponse update(
            UUID id,
            UpdateProfileRequest request) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        user.updateProfile(
                request.name(),
                request.email()
        );

        User saved = repository.save(user);

        return mapper.toResponse(saved);
    }
}

