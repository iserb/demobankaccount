package com.exampledemo.bankaccount.service;

import com.exampledemo.bankaccount.config.audit.AuditConstants;
import com.exampledemo.bankaccount.controller.exception.BankAccountInsufficientBalanceException;
import com.exampledemo.bankaccount.controller.exception.TransactionFailedException;
import com.exampledemo.bankaccount.dto.BankAccountDto;
import com.exampledemo.bankaccount.model.BankAccount;
import com.exampledemo.bankaccount.repository.BankAccountRepository;
import com.exampledemo.bankaccount.repository.CustomAuditEventRepository;
import com.exampledemo.bankaccount.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

@Service
public class BankAccountService {

    private static final Logger log = LoggerFactory.getLogger(BankAccountService.class);
    private final BankAccountRepository bankAccountRepository;
    private final CustomAuditEventRepository customAuditEventRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, CustomAuditEventRepository customAuditEventRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.customAuditEventRepository = customAuditEventRepository;
    }

    public BankAccountDto createAccount(BankAccountDto accountDto){
        BankAccount acc = bankAccountRepository.save(BankAccount.fromDto(accountDto));
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
            bankAccountRepository.save(acc);
        }
        catch (Exception ex){
            log.error("Error activating or deactivating account {}", id, ex);
            throw new TransactionFailedException("Transaction failed during activating or deactivating", HttpStatus.CONFLICT);
        }
        return true;
    }

    @Transactional(isolation=REPEATABLE_READ, rollbackFor = TransactionFailedException.class)
    public BankAccountDto changeBalance(Long id, BigInteger change, String reason){
        // -500 for decrease
        BankAccount acc = bankAccountRepository.getOne(id);
        if (acc.getBalance().add(change).signum()<0){
            //audit
            AuditEvent ev = new AuditEvent(SecurityUtils.getCurrentUserLogin(), String.valueOf(AuditConstants.FAILED_TRANSACTION),
                    fillAuditDataMap(id, change, "failed", "Insufficient balance"));
            customAuditEventRepository.add(ev);
            throw new BankAccountInsufficientBalanceException("Insufficient balance", HttpStatus.CONFLICT);
        }
        try {
            acc.setBalance(acc.getBalance().add(change));
            BankAccount saved = bankAccountRepository.save(acc);
            //audit
            AuditEvent ev = new AuditEvent(SecurityUtils.getCurrentUserLogin(), String.valueOf(AuditConstants.SUCCESSFUL_TRANSACTION),
                    fillAuditDataMap(id, change, "success", reason));
            customAuditEventRepository.add(ev);
            return BankAccountDto.of(saved);
        }
        catch (RuntimeException ex){
            log.error("Error modifying balance", ex);
            //audit
            AuditEvent ev = new AuditEvent(SecurityUtils.getCurrentUserLogin(), String.valueOf(AuditConstants.FAILED_TRANSACTION),
                    fillAuditDataMap(id, change, "failed", ex.getMessage()));
            customAuditEventRepository.add(ev);
            throw new TransactionFailedException("Transaction failed during balance change", HttpStatus.CONFLICT);
        }
    }

    private Map<String, Object> fillAuditDataMap(Long id, BigInteger change, String status, String reason) {
        Map<String, Object> data = new HashMap<>();
        data.put("account_id", id);
        data.put("amount", change.longValue());
        data.put("status", status);
        data.put("reason", reason);
        return data;
    }
}
