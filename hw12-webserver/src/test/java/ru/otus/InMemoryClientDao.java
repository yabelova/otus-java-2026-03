package ru.otus;

import ru.otus.dao.ClientDao;
import ru.otus.model.Client;

import java.util.ArrayList;
import java.util.List;

public class InMemoryClientDao implements ClientDao {

    private final List<Client> clients = new ArrayList<>();
    private long nextId = 1;

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(clients);
    }

    @Override
    public Client save(Client client) {
        if (client.getId() == null) {
            client.setId(nextId++);
        }
        clients.add(client);
        return client;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void clear() {
        clients.clear();
    }
}
