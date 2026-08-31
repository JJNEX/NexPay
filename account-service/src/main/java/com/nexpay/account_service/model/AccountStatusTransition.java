package com.nexpay.account_service.model;

public final class AccountStatusTransition {
    
private AccountStatusTransition() {
    }

    public static boolean canTransition(
            AccountStatus currentStatus,
            AccountStatus nextStatus) {

        return switch (currentStatus) {

            case ACTIVE ->
                    nextStatus == AccountStatus.BLOCKED;

            case BLOCKED ->
                    nextStatus == AccountStatus.ACTIVE
                    || nextStatus == AccountStatus.CLOSED;

            case CLOSED ->
                    false;
        };
    }
    
}
