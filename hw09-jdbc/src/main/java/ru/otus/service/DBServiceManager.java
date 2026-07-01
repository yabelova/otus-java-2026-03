/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L18-jdbc)
 * Adapted for the hw09-jdbc module structure.
 */
package ru.otus.service;

import ru.otus.model.Manager;
import java.util.List;
import java.util.Optional;

public interface DBServiceManager {
    Manager saveManager(Manager client);
    Optional<Manager> getManager(long no);
    List<Manager> findAll();
}
