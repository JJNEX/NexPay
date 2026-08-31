package com.nexpay.account_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.nexpay.account_service.dto.AccountRequest;
import com.nexpay.account_service.dto.AccountResponse;
import com.nexpay.account_service.model.AccountStatus;
import com.nexpay.account_service.service.InterfaceAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final InterfaceAccountService service;

    @PostMapping
    public ResponseEntity<AccountResponse> create(
            @Valid @RequestBody AccountRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                service.findById(id)
        );
    }

    @GetMapping("/number/{accountNumber}")
public ResponseEntity<AccountResponse> findByAccountNumber(
        @PathVariable String accountNumber) {

    return ResponseEntity.ok(
            service.findByAccountNumber(accountNumber)
    );
}

    @GetMapping
    public ResponseEntity<Page<AccountResponse>> findAll(
        @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                service.findAll(pageable)
        );
    }
    
    @GetMapping("/status")
    public ResponseEntity<Page<AccountResponse>> findByStatus(
            @RequestParam AccountStatus status,   
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                service.findByStatus(status, pageable)
        );
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponse>> findByCustomerId(
            @PathVariable UUID customerId) {

        return ResponseEntity.ok(
                service.findByCustomerId(customerId)
        );
    }


    @GetMapping("/customer/{customerId}/status")
    public ResponseEntity<List<AccountResponse>> findByCustomerIdAndStatus(
            @PathVariable UUID customerId,
            @RequestParam AccountStatus status) {

        return ResponseEntity.ok(
                service.findByCustomerIdAndStatus(
                        customerId,
                        status
                )
        );
    }

    @PatchMapping("/{id}/activate")
public ResponseEntity<AccountResponse> activateAccount(
        @PathVariable UUID id) {

    return ResponseEntity.ok(
            service.activateAccount(id)
    );
}

@PatchMapping("/{id}/block")
public ResponseEntity<AccountResponse> blockAccount(
        @PathVariable UUID id) {

    return ResponseEntity.ok(
            service.blockAccount(id)
    );
}

@PatchMapping("/{id}/close")
public ResponseEntity<AccountResponse> closeAccount(
        @PathVariable UUID id) {

    return ResponseEntity.ok(
            service.closeAccount(id)
    );
}
}