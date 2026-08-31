package com.nexpay.account_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.nexpay.account_service.model.AccountStatus;
import com.nexpay.account_service.model.AccountType;

public record AccountResponse (

        UUID id,

        UUID customerId,

        String accountNumber,

        BigDecimal balance,

        AccountStatus status,

        AccountType type,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
)
{
    
}


       
