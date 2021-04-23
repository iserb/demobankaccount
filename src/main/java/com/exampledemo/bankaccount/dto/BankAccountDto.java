package com.exampledemo.bankaccount.dto;

import com.exampledemo.bankaccount.model.BankAccount;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
public class BankAccountDto {
    @NotNull
    Long userId;
    BigInteger balance;
    @NotBlank
    String currency;
    Boolean isActive;

    public static BankAccountDto of(BankAccount bankAccount){
        BankAccountDto dto = new BankAccountDto();
        dto.setBalance(bankAccount.getBalance());
        dto.setUserId(bankAccount.getUserId());
        dto.setCurrency(bankAccount.getCurrency());
        dto.setIsActive(bankAccount.getIsActive());
        return dto;
    }
}
