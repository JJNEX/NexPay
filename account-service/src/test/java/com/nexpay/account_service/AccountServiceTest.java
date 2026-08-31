package com.nexpay.account_service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

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
import com.nexpay.account_service.service.AccountService;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @Mock
    private AccountMapper mapper;

    @Mock
    private AccountStatusValidator statusValidator;

    @InjectMocks
    private AccountService service;

    @Test
void shouldCreateAccountSuccessfully() {

    UUID customerId = UUID.randomUUID();

    AccountRequest request = mock(AccountRequest.class);

    Account account = new Account();

    AccountResponse response = mock(AccountResponse.class);

    when(request.customerId())
            .thenReturn(customerId);

    when(repository.existsByCustomerIdAndType(
            any(),
            any()))
            .thenReturn(false);

    when(repository.getNextAccountNumber())
            .thenReturn(1L);

    when(mapper.toEntity(request))
            .thenReturn(account);

    when(repository.save(account))
            .thenReturn(account);

    when(mapper.toResponse(account))
            .thenReturn(response);

    AccountResponse result = service.create(request);

    assertNotNull(result);

    assertEquals(
            "000000001",
            account.getAccountNumber()
    );

    verify(repository)
            .existsByCustomerIdAndType(
                    any(),
                    any()
            );

    verify(repository)
            .getNextAccountNumber();

    verify(repository)
            .save(account);

    verify(mapper)
            .toEntity(request);

    verify(mapper)
            .toResponse(account);
}

    @Test
    void shouldThrowExceptionWhenCustomerAlreadyHasAccountType() {

        AccountRequest request = mock(AccountRequest.class);

        UUID customerId = UUID.randomUUID();

        when(request.customerId()).thenReturn(customerId);

        when(repository.existsByCustomerIdAndType(
                any(),
                any()))
                .thenReturn(true);

        assertThrows(
                AccountAlreadyExistsException.class,
                () -> service.create(request)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void shouldFindAccountById() {

        UUID id = UUID.randomUUID();

        Account account = new Account();
        account.setId(id);

        AccountResponse response = mock(AccountResponse.class);

        when(repository.findById(id))
                .thenReturn(Optional.of(account));

        when(mapper.toResponse(account))
                .thenReturn(response);

        AccountResponse result = service.findById(id);

        assertNotNull(result);

        verify(repository).findById(id);
        verify(mapper).toResponse(account);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundById() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> service.findById(id)
        );
    }

    @Test
    void shouldFindAccountByAccountNumber() {

        String accountNumber = "000000001";

        Account account = new Account();

        AccountResponse response = mock(AccountResponse.class);

        when(repository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        when(mapper.toResponse(account))
                .thenReturn(response);

        AccountResponse result =
                service.findByAccountNumber(accountNumber);

        assertNotNull(result);

        verify(repository)
                .findByAccountNumber(accountNumber);
    }

    @Test
    void shouldChangeStatusSuccessfully() {

        UUID id = UUID.randomUUID();

        Account account = new Account();
        account.setStatus(AccountStatus.ACTIVE);

        AccountResponse response = mock(AccountResponse.class);

        when(repository.findById(id))
                .thenReturn(Optional.of(account));

        when(repository.save(account))
                .thenReturn(account);

        when(mapper.toResponse(account))
                .thenReturn(response);

        AccountResponse result =
                service.changeStatus(
                        id,
                        AccountStatus.BLOCKED
                );

        assertNotNull(result);

        assertEquals(
                AccountStatus.BLOCKED,
                account.getStatus()
        );

        verify(statusValidator)
                .validate(
                        AccountStatus.ACTIVE,
                        AccountStatus.BLOCKED
                );

        verify(repository).save(account);
    }

    @Test
    void shouldActivateAccount() {

        UUID id = UUID.randomUUID();

        Account account = new Account();
        account.setStatus(AccountStatus.BLOCKED);

        AccountResponse response = mock(AccountResponse.class);

        when(repository.findById(id))
                .thenReturn(Optional.of(account));

        when(repository.save(account))
                .thenReturn(account);

        when(mapper.toResponse(account))
                .thenReturn(response);

        service.activateAccount(id);

        assertEquals(
                AccountStatus.ACTIVE,
                account.getStatus()
        );
    }

    @Test
    void shouldBlockAccount() {

        UUID id = UUID.randomUUID();

        Account account = new Account();
        account.setStatus(AccountStatus.ACTIVE);

        AccountResponse response = mock(AccountResponse.class);

        when(repository.findById(id))
                .thenReturn(Optional.of(account));

        when(repository.save(account))
                .thenReturn(account);

        when(mapper.toResponse(account))
                .thenReturn(response);

        service.blockAccount(id);

        assertEquals(
                AccountStatus.BLOCKED,
                account.getStatus()
        );
    }

    @Test
    void shouldCloseAccountWhenBalanceIsZero() {

        UUID id = UUID.randomUUID();

        Account account = new Account();

        account.setStatus(AccountStatus.BLOCKED);
        account.setBalance(BigDecimal.ZERO);

        AccountResponse response = mock(AccountResponse.class);

        when(repository.findById(id))
                .thenReturn(Optional.of(account));

        when(repository.save(account))
                .thenReturn(account);

        when(mapper.toResponse(account))
                .thenReturn(response);

        service.closeAccount(id);

        assertEquals(
                AccountStatus.CLOSED,
                account.getStatus()
        );
    }

    @Test
    void shouldThrowExceptionWhenClosingAccountWithBalance() {

        UUID id = UUID.randomUUID();

        Account account = new Account();

        account.setStatus(AccountStatus.BLOCKED);
        account.setBalance(new BigDecimal("100.00"));

        when(repository.findById(id))
                .thenReturn(Optional.of(account));

        assertThrows(
                InvalidAccountStateException.class,
                () -> service.closeAccount(id)
        );

        verify(repository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenClosingAccountNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> service.closeAccount(id)
        );
    }
}