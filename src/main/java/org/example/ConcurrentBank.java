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
        synchronized (accountFrom) {
            synchronized (accountTo) {
                accountFrom.withdraw(sumOfTransfer);
                accountTo.deposit(sumOfTransfer);
            }
        }
    }

    public BigDecimal getTotalBalance() {
        return billList.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
