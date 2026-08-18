package ru.otus.dao;

import ru.otus.model.Client;

import java.util.List;

public interface ClientDao {

    List<Client> findAll();

    Client save(Client client);
}
