package org.example;

import java.math.BigDecimal;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentBank {
    CopyOnWriteArrayList<BankAccount> billList = new CopyOnWriteArrayList<>();

    public BankAccount createAccount(BigDecimal cash) {
        BankAccount bankAccount = new BankAccount(cash);
        billList.add(bankAccount);
        return bankAccount;
    }

    public void transfer(BankAccount accountFrom,
                         BankAccount accountTo,
                         BigDecimal sumOfTransfer) {

        BankAccount lockFrom = accountFrom.getId().get() > accountTo.getId().get() ? accountFrom : accountTo;
        BankAccount lockTo = accountFrom.getId().get() > accountTo.getId().get() ? accountTo : accountFrom;

        if(lockFrom.equals(lockTo)) {
            throw new IllegalStateException("Нельзя выполнить перевод самому себе!");
        } else {
            synchronized (lockFrom) {
                synchronized (lockTo) {
                    if (accountFrom.getBalance().compareTo(sumOfTransfer) >= 0) {
                        accountFrom.withdraw(sumOfTransfer);
                        accountTo.deposit(sumOfTransfer);
                    } else {
                        throw new IllegalArgumentException("Недостаточно средств на счете!");
                    }
                }
            }
        }
    }

    public BigDecimal getTotalBalance() {
        return billList.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

