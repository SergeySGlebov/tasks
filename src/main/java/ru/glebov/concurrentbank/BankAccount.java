package ru.glebov.concurrentbank;

public class BankAccount {
    private int id;
    private int balance;

    public BankAccount(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int value) {
        this.balance += value;
    }

    public void withdraw(int value) {
        int balanceAfterChange = this.balance - value;
        if (balanceAfterChange < 0) {
            throw new OutOfBalance("На счете недостаточно средств");
        }
        this.balance = balanceAfterChange;
    }
}
