/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L18-jdbc)
 * Adapted for the hw09-jdbc module structure.
 */
package ru.otus.jdbc;

import java.sql.Connection;
import java.util.function.Function;

public interface TransactionAction<T> extends Function<Connection, T> {
}
