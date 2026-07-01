/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L18-jdbc)
 * Adapted for the hw09-jdbc module structure.
 */
package ru.otus.jdbc;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface DataTemplate<T> {
    Optional<T> findById(Connection connection, long id);
    List<T> findAll(Connection connection);
    long insert(Connection connection, T object);
    void update(Connection connection, T object);
}
