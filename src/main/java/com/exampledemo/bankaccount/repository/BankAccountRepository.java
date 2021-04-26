package com.exampledemo.bankaccount.repository;

import com.exampledemo.bankaccount.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Override
    BankAccount getOne(Long id);

}
