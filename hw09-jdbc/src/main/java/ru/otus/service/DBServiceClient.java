/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L18-jdbc)
 * Adapted for the hw09-jdbc module structure.
 */
package ru.otus.service;

import ru.otus.model.Client;
import java.util.List;
import java.util.Optional;

public interface DBServiceClient {
    Client saveClient(Client client);
    Optional<Client> getClient(long id);
    List<Client> findAll();
}
