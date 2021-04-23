package com.exampledemo.bankaccount.model;

import com.exampledemo.bankaccount.dto.BankAccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="currency")
    String currency;
    @Column(name="balance")
    BigInteger balance; //в копейках
    @Column(name="user_id")
    Long userId;
    @Column(name="create_time")
    Instant created = Instant.now();
    @Column(name="update_time")
    Instant updated = Instant.now();
    @Column(name="is_active")
    Boolean isActive;

    public static BankAccount fromDto(BankAccountDto dto){
        BankAccount acc = new BankAccount();
        acc.setBalance(null!=dto.getBalance() ? dto.getBalance() : new BigInteger(String.valueOf(0L)));
        acc.setUserId(dto.getUserId());
        acc.setCurrency(dto.getCurrency());
        acc.setIsActive(null!=dto.getIsActive() ? dto.getIsActive() : true);
        acc.setCreated(Instant.now());
        acc.setUpdated(Instant.now());
        return acc;
    }
}
