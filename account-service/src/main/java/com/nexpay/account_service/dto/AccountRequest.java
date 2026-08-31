package com.nexpay.account_service.dto;

import java.util.UUID;

import com.nexpay.account_service.model.AccountType;

import jakarta.validation.constraints.NotNull;

public record AccountRequest (

    @NotNull(message = "ID do cliente é obrigatório")
        UUID customerId,

        @NotNull(message = "Tipo da conta é obrigatório")
        AccountType type


){
    
}


