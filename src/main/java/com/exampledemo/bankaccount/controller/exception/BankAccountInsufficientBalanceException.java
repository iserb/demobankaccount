package com.exampledemo.bankaccount.controller.exception;

import org.springframework.http.HttpStatus;

public class BankAccountInsufficientBalanceException extends BankAccountException{
    public BankAccountInsufficientBalanceException(String message, HttpStatus status) {
        super(message, status);
    }
}
