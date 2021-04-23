package com.exampledemo.bankaccount.controller.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public abstract class BankAccountException extends RuntimeException{
    @Getter
    private HttpStatus httpStatus;
    @Getter
    private String message;

    public BankAccountException(String message, HttpStatus status){
        this.httpStatus=status;
        this.message = message;
    }
}
