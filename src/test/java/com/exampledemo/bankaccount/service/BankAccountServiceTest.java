package com.exampledemo.bankaccount.service;

import com.exampledemo.bankaccount.dto.BankAccountDto;
import com.exampledemo.bankaccount.model.BankAccount;
import com.exampledemo.bankaccount.repository.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Random;

@RunWith(SpringRunner.class)
@ActiveProfiles("localhost")
@SpringBootTest
class BankAccountServiceTest {

    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private BankAccountRepository bankAccountRepository;


    @Test
    @Transactional
    void createAccount() {
        BankAccountDto dto = new BankAccountDto();
        dto.setUserId(getRandomUserId());
        dto.setCurrency("RUB");
        BankAccountDto result = bankAccountService.createAccount(dto);
        BankAccount acc = bankAccountRepository.getOne(result.getId());
        assert(null!=acc.getId());
    }

    @Test
    @Transactional
    void getCreatedAccount() {
        BankAccountDto dto = new BankAccountDto();
        Long randomUser = getRandomUserId();
        dto.setUserId(randomUser);
        dto.setCurrency("RUB");
        BankAccountDto result = bankAccountService.createAccount(dto);
        BankAccount acc = bankAccountRepository.getOne(result.getId());
        assert(null!=acc.getId());
        assert(randomUser.equals(acc.getUserId()));
        assert(BigInteger.ZERO.equals(acc.getBalance()));
    }

    @Test
    @Transactional
    void activateOrDeactivateAccount() {
        BankAccountDto dto = new BankAccountDto();
        Long randomUser = getRandomUserId();
        dto.setUserId(randomUser);
        dto.setCurrency("RUB");
        BankAccountDto result = bankAccountService.createAccount(dto);
        BankAccount acc = bankAccountRepository.getOne(result.getId());
        assert(null!=acc.getId());
        bankAccountService.activateOrDeactivateAccount(acc.getId(), false);
        acc = bankAccountRepository.getOne(result.getId());
        assert(Boolean.FALSE.equals(acc.getIsActive()));
        bankAccountService.activateOrDeactivateAccount(acc.getId(), true);
        acc = bankAccountRepository.getOne(result.getId());
        assert(Boolean.TRUE.equals(acc.getIsActive()));
    }

    @Test
    @Transactional
    void changeBalance() {
        BankAccountDto dto = new BankAccountDto();
        Long randomUser = getRandomUserId();
        dto.setUserId(randomUser);
        dto.setCurrency("RUB");
        BankAccountDto result = bankAccountService.createAccount(dto);
        BankAccount acc = bankAccountRepository.getOne(result.getId());
        assert(null!=acc.getId());
        bankAccountService.changeBalance(acc.getId(), BigInteger.valueOf(100), "TestAdd");
        acc = bankAccountRepository.getOne(result.getId());
        assert(BigInteger.valueOf(100).equals(acc.getBalance()));
        bankAccountService.changeBalance(acc.getId(), BigInteger.valueOf(-50), "TestRemove");
        acc = bankAccountRepository.getOne(result.getId());
        assert(BigInteger.valueOf(50).equals(acc.getBalance()));
    }

    private Long getRandomUserId(){
        int leftLimit = 1;
        int rightLimit = 10;
        long generatedLong = leftLimit + (long) (new Random().nextFloat() * (rightLimit - leftLimit));
        return generatedLong;
    }
}