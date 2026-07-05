/*
 * Source: Module hw09-jdbc
 */
package ru.otus.jdbc;

import java.sql.Connection;
import java.util.function.Function;

public interface TransactionAction<T> extends Function<Connection, T> {
}
