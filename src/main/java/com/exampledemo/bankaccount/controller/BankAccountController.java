package com.exampledemo.bankaccount.controller;

import com.exampledemo.bankaccount.dto.BankAccountChangeBalance;
import com.exampledemo.bankaccount.dto.BankAccountChangeStatus;
import com.exampledemo.bankaccount.dto.BankAccountDto;
import com.exampledemo.bankaccount.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDto> getAccount(@PathVariable Long id){
        return new ResponseEntity<>(bankAccountService.getAccount(id), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<BankAccountDto> getAccount(@RequestBody BankAccountDto dto){
        return new ResponseEntity<>(bankAccountService.createAccount(dto), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<Boolean> changeStatus(@PathVariable Long id, @RequestBody BankAccountChangeStatus isActive){
        Boolean result = bankAccountService.activateOrDeactivateAccount(id, isActive.getIsActive());
        return new ResponseEntity<>(result, result ? HttpStatus.OK : HttpStatus.CONFLICT);
    }

    @PostMapping("/{id}/change-balance")
    public ResponseEntity<BankAccountDto> changeBalance(@PathVariable Long id, @RequestBody BankAccountChangeBalance accountChangeBalance){
        BankAccountDto result = bankAccountService.changeBalance(id, accountChangeBalance.getAmount(), accountChangeBalance.getReason());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
