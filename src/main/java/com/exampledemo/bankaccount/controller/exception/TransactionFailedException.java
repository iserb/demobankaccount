package com.exampledemo.bankaccount.controller.exception;

import org.springframework.http.HttpStatus;

public class TransactionFailedException extends BankAccountException{
    public TransactionFailedException(String message, HttpStatus status) {
        super(message, status);
    }
}
