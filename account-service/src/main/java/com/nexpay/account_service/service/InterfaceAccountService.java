package com.nexpay.account_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexpay.account_service.dto.AccountRequest;
import com.nexpay.account_service.dto.AccountResponse;
import com.nexpay.account_service.model.AccountStatus;

public interface InterfaceAccountService {

    AccountResponse create(AccountRequest request);

    AccountResponse findById(UUID id);

    AccountResponse findByAccountNumber(String accountNumber);

    List<AccountResponse> findByCustomerId(UUID customerId);

    List<AccountResponse> findByCustomerIdAndStatus(UUID customerId, AccountStatus status);

    Page<AccountResponse> findByStatus(AccountStatus status, Pageable pageable);

    Page<AccountResponse> findAll(Pageable pageable);

    AccountResponse activateAccount(UUID id);

    AccountResponse blockAccount(UUID id);

    AccountResponse closeAccount(UUID id);

}
