package com.nexpay.auth_service.auth.dto;

public record AuthResponse(

        String accessToken,

        String tokenType

) {}