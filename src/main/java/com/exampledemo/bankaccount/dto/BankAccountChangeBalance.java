package com.exampledemo.bankaccount.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class BankAccountChangeBalance {
    BigInteger amount;
    String reason;
}
