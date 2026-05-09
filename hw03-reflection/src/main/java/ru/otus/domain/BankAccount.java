package ru.otus.domain;

import ru.otus.domain.exceptions.BalanceExceededException;
import ru.otus.domain.exceptions.IllegalAmountException;

/**
 * Simple bank account with balance management.
 */
public class BankAccount {

    private int balance;

    public int getBalance() {
        return balance;
    }

    public BankAccount(int amount) {
        if (amount < 0)
            throw new IllegalAmountException("Shouldn't be less than 0");
        this.balance = amount;
    }

    public void deposit(int amount) {
        if (amount < 0)
            throw new IllegalAmountException("Shouldn't be less than 0");
        balance += amount;
    }

    public void withdraw(int amount) {
        if (amount < 0)
            throw new IllegalAmountException("Shouldn't be less than 0");
        if (amount > balance)
            throw new BalanceExceededException("Balance exceeded. Max: " + balance + ", Requested: " + amount);
        balance -= amount;
    }
}