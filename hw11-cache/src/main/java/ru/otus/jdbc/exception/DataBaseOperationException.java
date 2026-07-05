/*
 * Source: Module hw09-jdbc
 */
package ru.otus.jdbc.exception;

public class DataBaseOperationException extends RuntimeException {
    public DataBaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
