package com.nexpay.account_service.model;

import org.springframework.stereotype.Component;

import com.nexpay.account_service.exception.InvalidAccountStateException;

@Component
public class AccountStatusValidator {

    public void validate(
            AccountStatus currentStatus,
            AccountStatus newStatus) {

        if (currentStatus == AccountStatus.ACTIVE
            && newStatus == AccountStatus.ACTIVE) {
        throw new InvalidAccountStateException(
                "A conta já está ativa.");
    }

        if (currentStatus == AccountStatus.BLOCKED
            && newStatus == AccountStatus.BLOCKED) {
        throw new InvalidAccountStateException(
                "A conta já está bloqueada.");
    }

     if (currentStatus == AccountStatus.CLOSED
            && newStatus == AccountStatus.ACTIVE) {
        throw new InvalidAccountStateException(
                "Não é possível reativar uma conta fechada.");
    }

    if (currentStatus == AccountStatus.ACTIVE
            && newStatus == AccountStatus.CLOSED) {
        throw new InvalidAccountStateException(
                "A conta deve ser bloqueada antes de ser encerrada.");
    }

    if (!AccountStatusTransition.canTransition(
                currentStatus,
                newStatus)) {

            throw new IllegalStateException(
                    "Transição inválida: "
                    + currentStatus
                    + " -> "
                    + newStatus
            );
        }


    }
}