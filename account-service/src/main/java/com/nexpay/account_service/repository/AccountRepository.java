package com.nexpay.account_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nexpay.account_service.model.Account;
import com.nexpay.account_service.model.AccountStatus;
import com.nexpay.account_service.model.AccountType;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByCustomerId(UUID customerId);
    List<Account> findByCustomerIdAndStatus(UUID customerId, AccountStatus status);
    Page<Account> findByStatus(AccountStatus status, Pageable pageable);
    Optional<Account> findByIdAndStatus(UUID id, AccountStatus status);
    boolean existsByCustomerIdAndType(UUID customerId, AccountType type);
    boolean existsByAccountNumber(String accountNumber);
    Optional<Account> findByAccountNumber(String accountNumber);
    @Query(value = "SELECT nextval('account_number_seq')", nativeQuery = true)
    Long getNextAccountNumber();

}