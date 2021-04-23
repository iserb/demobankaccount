package com.exampledemo.bankaccount.service;

import com.exampledemo.bankaccount.controller.exception.BankAccountInsufficientBalanceException;
import com.exampledemo.bankaccount.controller.exception.TransactionFailedException;
import com.exampledemo.bankaccount.dto.BankAccountDto;
import com.exampledemo.bankaccount.model.BankAccount;
import com.exampledemo.bankaccount.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

@Service
public class BankAccountService {

    private static final Logger log = LoggerFactory.getLogger(BankAccountService.class);
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccountDto createAccount(BankAccountDto accountDto){
        BankAccount acc = bankAccountRepository.saveAndFlush(BankAccount.fromDto(accountDto));
        return BankAccountDto.of(acc);
    }

    public BankAccountDto getAccount(Long id){
        BankAccount acc = bankAccountRepository.getOne(id);
        return BankAccountDto.of(acc);
    }

    @Transactional(isolation=REPEATABLE_READ, rollbackFor = TransactionFailedException.class)
    public Boolean activateOrDeactivateAccount(Long id, Boolean isActive){

        BankAccount acc = bankAccountRepository.getOne(id);
        try {
            acc.setIsActive(isActive);
            bankAccountRepository.saveAndFlush(acc);
        }
        catch (Exception ex){
            log.error("Error activating or deactivating account {}", id, ex);
            throw new TransactionFailedException("Transaction failed during activating or deactivating", HttpStatus.CONFLICT);
        }
        return true;
    }

    @Transactional(isolation=REPEATABLE_READ, rollbackFor = TransactionFailedException.class)
    public BankAccountDto changeBalance(Long id, BigInteger change){
        // -500 for decrease
        BankAccount acc = bankAccountRepository.getOne(id);
        if (acc.getBalance().add(change).signum()<0){
            throw new BankAccountInsufficientBalanceException("Insufficient balance", HttpStatus.CONFLICT);
        }
        try {
            acc.setBalance(acc.getBalance().add(change));
            BankAccount saved = bankAccountRepository.saveAndFlush(acc);
            return BankAccountDto.of(saved);
        }
        catch (RuntimeException ex){
            log.error("Error modifying balance", ex);
            throw new TransactionFailedException("Transaction failed during balance change", HttpStatus.CONFLICT);
        }
    }
}
