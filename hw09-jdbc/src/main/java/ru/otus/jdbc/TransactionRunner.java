/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L18-jdbc)
 * Adapted for the hw09-jdbc module structure.
 */
package ru.otus.jdbc;

public interface TransactionRunner {
    <T> T doInTransaction(TransactionAction<T> action);
}
