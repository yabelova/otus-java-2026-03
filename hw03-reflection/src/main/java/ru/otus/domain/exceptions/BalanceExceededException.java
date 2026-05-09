package ru.otus.domain.exceptions;

public class BalanceExceededException extends RuntimeException {
    public BalanceExceededException(String message) {
        super(message);
    }
}