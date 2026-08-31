package com.nexpay.account_service.exceptionHandler;

import com.nexpay.account_service.dto.ApiErrorResponse;
import com.nexpay.account_service.dto.FieldValidationError;
import com.nexpay.account_service.exception.AccountAlreadyExistsException;

import com.nexpay.account_service.exception.AccountNotFoundException;
import com.nexpay.account_service.exception.InvalidAccountStateException;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            WebRequest request
    ) {
        List<FieldValidationError> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .toList();

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Existem campos inválidos na requisição.",
                extractPath(request),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleHandlerMethodValidation(
            HandlerMethodValidationException exception,
            WebRequest request
    ) {
        List<FieldValidationError> errors = new ArrayList<>();

        exception.getValueResults().forEach(validationResult -> {
            String fieldName = validationResult.getMethodParameter().getParameterName();

            validationResult.getResolvableErrors().forEach(error -> {
                errors.add(new FieldValidationError(
                        fieldName,
                        error.getDefaultMessage()
                ));
            });
        });

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Existem parâmetros inválidos na requisição.",
                extractPath(request),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception,
            WebRequest request
    ) {
        List<FieldValidationError> errors = exception.getConstraintViolations()
                .stream()
                .map(violation -> new FieldValidationError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ))
                .toList();

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Existem parâmetros inválidos na requisição.",
                extractPath(request),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFound(
            NoResourceFoundException exception,
            WebRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                "Recurso não encontrado.",
                extractPath(request),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception exception,
            WebRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "Ocorreu um erro interno no servidor.",
                extractPath(request),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatus(
            ResponseStatusException exception,
            WebRequest request
    ) {
        int statusCode = exception.getStatusCode().value();
        String message = exception.getReason();
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                statusCode,
                exception.getStatusCode().toString(),
                message,
                extractPath(request),
                List.of()
        );

        return ResponseEntity.status(statusCode).body(response);
    }

    @ExceptionHandler(AccountNotFoundException.class)
public ResponseEntity<ApiErrorResponse> handleAccountNotFound(
        AccountNotFoundException exception,
        WebRequest request
) {

    ApiErrorResponse response = new ApiErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "ACCOUNT_NOT_FOUND",
            exception.getMessage(),
            extractPath(request),
            List.of()
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response);
}

@ExceptionHandler(AccountAlreadyExistsException.class)
public ResponseEntity<ApiErrorResponse> handleAccountAlreadyExists(
        AccountAlreadyExistsException exception,
        WebRequest request
) {

    ApiErrorResponse response = new ApiErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "ACCOUNT_ALREADY_EXISTS",
            exception.getMessage(),
            extractPath(request),
            List.of()
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(response);
}

@ExceptionHandler(InvalidAccountStateException.class)
public ResponseEntity<ApiErrorResponse> handleInvalidAccountState(
        InvalidAccountStateException exception,
        WebRequest request
) {

    ApiErrorResponse response = new ApiErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "INVALID_ACCOUNT_STATE",
            exception.getMessage(),
            extractPath(request),
            List.of()
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(response);
}

    private FieldValidationError mapFieldError(FieldError fieldError) {
        return new FieldValidationError(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );
    }

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}