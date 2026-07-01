/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L18-jdbc)
 * Adapted for the hw09-jdbc module structure.
 */
package ru.otus.jdbc.exception;

public class DataBaseOperationException extends RuntimeException {
    public DataBaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
