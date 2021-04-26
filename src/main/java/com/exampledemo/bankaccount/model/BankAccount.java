package com.exampledemo.bankaccount.model;

import com.exampledemo.bankaccount.dto.BankAccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.Instant;

@Entity
@Table(name="bank_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BankAccount extends AbstractAuditedEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="currency")
    String currency;
    @Column(name="balance")
    BigInteger balance; //в копейках
    @Column(name="user_id")
    Long userId;

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
