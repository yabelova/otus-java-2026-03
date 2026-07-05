/*
 * Source: Module hw09-jdbc
 */
package ru.otus.jdbc;

public interface TransactionRunner {
    <T> T doInTransaction(TransactionAction<T> action);
}
