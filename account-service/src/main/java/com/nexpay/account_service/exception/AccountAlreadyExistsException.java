package com.nexpay.account_service.exception;

public class AccountAlreadyExistsException
        extends RuntimeException {

    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}