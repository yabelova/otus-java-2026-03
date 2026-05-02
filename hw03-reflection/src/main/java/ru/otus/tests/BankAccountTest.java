package ru.otus.tests;

import ru.otus.domain.BankAccount;
import ru.otus.domain.exceptions.BalanceExceededException;
import ru.otus.domain.exceptions.IllegalAmountException;
import ru.otus.framework.annotations.After;
import ru.otus.framework.annotations.Before;
import ru.otus.framework.annotations.Test;
import ru.otus.framework.engine.Assertions;

public class BankAccountTest {

    private BankAccount bankAccount;

    @Before(order = 1)
    private void setupAccount() {
        bankAccount = new BankAccount(1000);
    }

    @Before(order = 2)
    private void logStart() {
        System.out.println(">>>Starting test. Current balance: " + bankAccount.getBalance());

    }

    @Test(displayName = "Deposit should increase balance")
    public void shouldIncreaseBalanceAfterDeposit() {
        bankAccount.deposit(500);
        Assertions.assertTrue(bankAccount.getBalance() == 1500, "Incorrect balance after deposit. Expected 1500, but got " + bankAccount.getBalance());
    }

    @Test(displayName = "Withdraw should decrease balance")
    public void shouldDecreaseBalanceAfterWithdraw() {
        bankAccount.withdraw(500);
        Assertions.assertTrue(bankAccount.getBalance() == 500, "Incorrect balance after withdraw. Expected 500, but got " + bankAccount.getBalance());
    }


    @Test(displayName = "Withdraw more than balance should throw exception")
    public void shouldThrowExceptionWhenBalanceExceeded() {
        Assertions.assertThrows(BalanceExceededException.class, () -> {
            bankAccount.withdraw(1100);
        });
    }

    @Test(displayName = "Negative deposit should throw exception")
    public void shouldThrowExceptionForNegativeDeposit() {
        Assertions.assertThrows(IllegalAmountException.class, () -> {
            bankAccount.deposit(-500);
        });
    }


    @Test(displayName = "Negative withdraw should throw exception")
    public void shouldThrowExceptionForNegativeWithdraw() {
        Assertions.assertThrows(IllegalAmountException.class, () -> {
            bankAccount.withdraw(-500);
        });
    }

    @Test(displayName = "Negative initial amount should throw exception")
    public void shouldThrowExceptionForNegativeInitialAmount() {
        Assertions.assertThrows(IllegalAmountException.class, () -> {
            new BankAccount(-1);
        });
    }

    @After
    private void logEnd() {
        System.out.println("<<<Test finished. Final balance: " + bankAccount.getBalance());
    }
}