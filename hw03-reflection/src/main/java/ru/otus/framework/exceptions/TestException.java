package ru.otus.framework.exceptions;

/**
 * Thrown by the framework or assertions when a test fail or an internal error occurs.
 */
public class TestException extends RuntimeException {
    public TestException(String message) {
        super(message);
    }
}