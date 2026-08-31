package com.nexpay.account_service.dto;

public record FieldValidationError(
        String field,
        String message
) {
}
