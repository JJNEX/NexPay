package com.nexpay.account_service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nexpay.account_service.exception.InvalidAccountStateException;
import com.nexpay.account_service.model.AccountStatus;
import com.nexpay.account_service.model.AccountStatusValidator;

class AccountStatusValidatorTest {

    private AccountStatusValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AccountStatusValidator();
    }

    @Test
    void shouldAllowActiveToBlocked() {

        assertDoesNotThrow(() ->
                validator.validate(
                        AccountStatus.ACTIVE,
                        AccountStatus.BLOCKED
                )
        );
    }

    @Test
    void shouldAllowBlockedToActive() {

        assertDoesNotThrow(() ->
                validator.validate(
                        AccountStatus.BLOCKED,
                        AccountStatus.ACTIVE
                )
        );
    }

    @Test
    void shouldAllowBlockedToClosed() {

        assertDoesNotThrow(() ->
                validator.validate(
                        AccountStatus.BLOCKED,
                        AccountStatus.CLOSED
                )
        );
    }

    @Test
    void shouldNotAllowActiveToClosed() {

        InvalidAccountStateException exception =
                assertThrows(
                        InvalidAccountStateException.class,
                        () -> validator.validate(
                                AccountStatus.ACTIVE,
                                AccountStatus.CLOSED
                        )
                );

        assertEquals(
                "A conta deve ser bloqueada antes de ser encerrada.",
                exception.getMessage()
        );
    }

    @Test
    void shouldNotAllowClosedToActive() {

        InvalidAccountStateException exception =
                assertThrows(
                        InvalidAccountStateException.class,
                        () -> validator.validate(
                                AccountStatus.CLOSED,
                                AccountStatus.ACTIVE
                        )
                );

        assertEquals(
                "Não é possível reativar uma conta fechada.",
                exception.getMessage()
        );
    }

    @Test
    void shouldNotAllowBlockedToBlocked() {

        InvalidAccountStateException exception =
                assertThrows(
                        InvalidAccountStateException.class,
                        () -> validator.validate(
                                AccountStatus.BLOCKED,
                                AccountStatus.BLOCKED
                        )
                );

        assertEquals(
                "A conta já está bloqueada.",
                exception.getMessage()
        );
    }

    @Test
    void shouldNotAllowActiveToActive() {

        InvalidAccountStateException exception =
                assertThrows(
                        InvalidAccountStateException.class,
                        () -> validator.validate(
                                AccountStatus.ACTIVE,
                                AccountStatus.ACTIVE
                        )
                );

        assertEquals(
                "A conta já está ativa.",
                exception.getMessage()
        );
    }
}