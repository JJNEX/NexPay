package com.nexpay.account_service.dto;

import com.nexpay.account_service.model.AccountStatus;

public record AccountRequestUpdate(

        AccountStatus status

) {}