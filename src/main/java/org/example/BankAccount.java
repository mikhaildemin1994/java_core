package org.example;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class BankAccount {
    private final AtomicInteger id;
    private BigDecimal balance;

    public BankAccount(BigDecimal cash) {
        this.id = new AtomicInteger(new Random().nextInt());
        this.balance = cash;
    }

    public synchronized void deposit(BigDecimal cashSum) {
        if(cashSum.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(cashSum);
        }
    }

    public synchronized void withdraw(BigDecimal needCashSum) {
        if(balance.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(needCashSum) >= 0) {
            balance = balance.subtract(needCashSum);
        }
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }
}
