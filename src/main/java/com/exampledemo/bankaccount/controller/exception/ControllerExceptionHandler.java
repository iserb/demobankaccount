package com.exampledemo.bankaccount.controller.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {BankAccountException.class})
    protected ResponseEntity<Object> handleTranslationConflict(
            BankAccountException ex, WebRequest request) {

        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getHttpStatus(), request);
    }
}