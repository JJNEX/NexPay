package com.nexpay.account_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexpay.account_service.dto.AccountRequest;
import com.nexpay.account_service.dto.AccountResponse;
import com.nexpay.account_service.exception.AccountAlreadyExistsException;
import com.nexpay.account_service.exception.AccountNotFoundException;
import com.nexpay.account_service.exception.InvalidAccountStateException;
import com.nexpay.account_service.mapper.AccountMapper;
import com.nexpay.account_service.model.Account;
import com.nexpay.account_service.model.AccountStatus;
import com.nexpay.account_service.model.AccountStatusValidator;
import com.nexpay.account_service.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements InterfaceAccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final AccountStatusValidator statusValidator;

    private String generateAccountNumber() {

        Long nextNumber = repository.getNextAccountNumber();

        return String.format("%09d", nextNumber);
    }

    @Override
    @Transactional
    public AccountResponse create(AccountRequest request) {

        boolean exists = repository.existsByCustomerIdAndType(
                request.customerId(),
                request.type());

        if (exists) {
            throw new AccountAlreadyExistsException(
                    "O cliente já possui uma conta do tipo " + request.type());
        }

        Account account = mapper.toEntity(request);

        account.assignAccountNumber(generateAccountNumber());

        Account saved = repository.save(account);

        return mapper.toResponse(saved);
    }

    @Override
    public AccountResponse findById(UUID id) {

        Account account = repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Conta não encontrada para o número: " + id));

        return mapper.toResponse(account);
    }

    @Override
    public AccountResponse findByAccountNumber(String accountNumber) {

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Conta não encontrada para o número: " + accountNumber));

        return mapper.toResponse(account);
    }

    @Override
    public List<AccountResponse> findByCustomerId(UUID customerId) {

        return repository.findByCustomerId(customerId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<AccountResponse> findByCustomerIdAndStatus(UUID customerId, AccountStatus status) {

        return repository.findByCustomerIdAndStatus(customerId, status)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public Page<AccountResponse> findAll(Pageable pageable) {

        return repository.findAll(pageable)
                .map(mapper::toResponse);

    }

    @Override
    public Page<AccountResponse> findByStatus(AccountStatus status, Pageable pageable) {

        return repository.findByStatus(status, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public AccountResponse changeStatus(UUID id, AccountStatus newStatus) {

        Account account = repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Conta não encontrada: " + id));

        statusValidator.validate(
                account.getStatus(),
                newStatus);

        account.changeStatus(newStatus);

        Account saved = repository.save(account);

        return mapper.toResponse(saved);
    }

    @Override
    public AccountResponse activateAccount(UUID id) {
        return changeStatus(id, AccountStatus.ACTIVE);
    }

    @Override
    public AccountResponse blockAccount(UUID id) {

        return changeStatus(id, AccountStatus.BLOCKED);
    }

    @Override
    @Transactional
    public AccountResponse closeAccount(UUID id) {

        Account account = repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Conta não encontrada: " + id));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new InvalidAccountStateException(
                    "A conta deve estar com saldo zerado para ser encerrada.");
        }

        return changeStatus(id, AccountStatus.CLOSED);
    }

}