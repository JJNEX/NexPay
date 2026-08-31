package com.nexpay.account_service.mapper;

import com.nexpay.account_service.dto.AccountRequest;
import com.nexpay.account_service.dto.AccountResponse;
import com.nexpay.account_service.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toEntity(AccountRequest request) {
        Account entity = new Account();

        entity.setCustomerId(request.customerId());
        entity.setType(request.type());
        
        return entity;
    }

    public AccountResponse toResponse(Account entity) {
        return new AccountResponse(
            entity.getId(),
            entity.getCustomerId(),
            entity.getAccountNumber(),
            entity.getBalance(),
            entity.getStatus(),
            entity.getType(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
                
        );
    }
} 
