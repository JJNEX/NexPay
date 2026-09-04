package com.nexpay.account_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexpay.account_service.dto.AccountRequest;
import com.nexpay.account_service.dto.AccountResponse;
import com.nexpay.account_service.exception.AccountAlreadyExistsException;
import com.nexpay.account_service.exception.AccountNotFoundException;
import com.nexpay.account_service.mapper.AccountMapper;
import com.nexpay.account_service.model.Account;
import com.nexpay.account_service.model.AccountStatus;
import com.nexpay.account_service.model.AccountStatusValidator;
import com.nexpay.account_service.model.AccountType;
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

                Account account = new Account(
                                customerId,
                                AccountType.CHECKING);

                AccountResponse response = mock(AccountResponse.class);

                when(request.customerId())
                                .thenReturn(customerId);

                when(request.type())
                                .thenReturn(AccountType.CHECKING);

                when(repository.existsByCustomerIdAndType(
                                customerId,
                                AccountType.CHECKING))
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
                                account.getAccountNumber());

                verify(repository)
                                .existsByCustomerIdAndType(
                                                customerId,
                                                AccountType.CHECKING);

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
        void shouldFindAccountById() {

                UUID id = UUID.randomUUID();

                Account account = new Account(
                                UUID.randomUUID(),
                                AccountType.CHECKING);

                AccountResponse response = mock(AccountResponse.class);

                when(repository.findById(id))
                                .thenReturn(Optional.of(account));

                when(mapper.toResponse(account))
                                .thenReturn(response);

                AccountResponse result = service.findById(id);

                assertNotNull(result);

                verify(repository)
                                .findById(id);

                verify(mapper)
                                .toResponse(account);
        }

        @Test
        void shouldThrowExceptionWhenAccountNotFoundById() {

                UUID id = UUID.randomUUID();

                when(repository.findById(id))
                                .thenReturn(Optional.empty());

                assertThrows(
                                AccountNotFoundException.class,
                                () -> service.findById(id));
        }

        @Test
        void shouldFindAccountByAccountNumber() {

                String accountNumber = "000000001";

                Account account = new Account(
                                UUID.randomUUID(),
                                AccountType.CHECKING);

                AccountResponse response = mock(AccountResponse.class);

                when(repository.findByAccountNumber(accountNumber))
                                .thenReturn(Optional.of(account));

                when(mapper.toResponse(account))
                                .thenReturn(response);

                AccountResponse result = service.findByAccountNumber(accountNumber);

                assertNotNull(result);

                verify(repository)
                                .findByAccountNumber(accountNumber);

                verify(mapper)
                                .toResponse(account);
        }

        @Test
        void shouldChangeStatusSuccessfully() {

                UUID id = UUID.randomUUID();

                Account account = new Account(
                                UUID.randomUUID(),
                                AccountType.CHECKING);

                AccountResponse response = mock(AccountResponse.class);

                when(repository.findById(id))
                                .thenReturn(Optional.of(account));

                when(repository.save(account))
                                .thenReturn(account);

                when(mapper.toResponse(account))
                                .thenReturn(response);

                AccountResponse result = service.changeStatus(
                                id,
                                AccountStatus.BLOCKED);

                assertNotNull(result);

                assertEquals(
                                AccountStatus.BLOCKED,
                                account.getStatus());

                verify(statusValidator)
                                .validate(
                                                AccountStatus.ACTIVE,
                                                AccountStatus.BLOCKED);

                verify(repository)
                                .save(account);
        }

        @Test
        void shouldActivateAccount() {

                UUID id = UUID.randomUUID();

                Account account = new Account(
                                UUID.randomUUID(),
                                AccountType.CHECKING);

                account.changeStatus(AccountStatus.BLOCKED);

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
                                account.getStatus());
        }

        @Test
        void shouldBlockAccount() {

                UUID id = UUID.randomUUID();

                Account account = new Account(
                                UUID.randomUUID(),
                                AccountType.CHECKING);

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
                                account.getStatus());
        }

        @Test
        void shouldCloseAccountWhenBalanceIsZero() {

                UUID id = UUID.randomUUID();

                Account account = new Account(
                                UUID.randomUUID(),
                                AccountType.CHECKING);

                account.changeStatus(AccountStatus.BLOCKED);

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
                                account.getStatus());
        }

        @Test
        void shouldThrowExceptionWhenClosingAccountNotFound() {

                UUID id = UUID.randomUUID();

                when(repository.findById(id))
                                .thenReturn(Optional.empty());

                assertThrows(
                                AccountNotFoundException.class,
                                () -> service.closeAccount(id));
        }

        @Test
        void shouldThrowExceptionWhenCustomerAlreadyHasAccountType() {

                AccountRequest request = mock(AccountRequest.class);

                UUID customerId = UUID.randomUUID();

                when(request.customerId())
                                .thenReturn(customerId);

                when(request.type())
                                .thenReturn(AccountType.CHECKING);

                when(repository.existsByCustomerIdAndType(
                                customerId,
                                AccountType.CHECKING))
                                .thenReturn(true);

                assertThrows(
                                AccountAlreadyExistsException.class,
                                () -> service.create(request));

                verify(repository, never())
                                .save(any());
        }
}