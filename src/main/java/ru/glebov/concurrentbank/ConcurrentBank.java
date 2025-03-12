package ru.glebov.concurrentbank;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentBank {
    private int lastIdOfBankAccount = 0;
    private final List<BankAccount> accounts = new ArrayList<>();

    public synchronized BankAccount createAccount(int amount) {
        BankAccount bankAccount = new BankAccount(++lastIdOfBankAccount, amount);
        accounts.add(bankAccount);
        return bankAccount;
    }

    public void transfer(BankAccount account1, BankAccount account2, int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Сумма операции не может быть отрицательной");
        }
        BankAccount first = account1;
        BankAccount second = account2;
        if (account1.getId() > account2.getId()) {
            first = account2;
            second = account1;
        }
        synchronized (first) {
            synchronized (second) {
                account1.withdraw(amount);
                account2.deposit(amount);
            }
        }
    }


    public synchronized int getTotalBalance() {
        return accounts.stream().map(BankAccount::getBalance).reduce(Integer::sum).orElse(0);
    }
}
