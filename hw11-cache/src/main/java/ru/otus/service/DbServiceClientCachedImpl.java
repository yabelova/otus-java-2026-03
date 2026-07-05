package ru.otus.service;

import ru.otus.cache.HwCache;
import ru.otus.cache.MyCache;
import ru.otus.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientCachedImpl implements DBServiceClient {

    private final DBServiceClient delegate;
    private final HwCache<Long, Client> cache;

    public DbServiceClientCachedImpl(DBServiceClient delegate) {
        this(delegate, new MyCache<>());
    }

    public DbServiceClientCachedImpl(DBServiceClient delegate, HwCache<Long, Client> cache) {
        this.delegate = delegate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        var saved = delegate.saveClient(client);
        cache.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var cached = cache.get(id);
        if (cached != null)
            return Optional.of(cached);

        var find = delegate.getClient(id);
        find.ifPresent(client -> cache.put(id, client));
        return find;
    }

    @Override
    public List<Client> findAll() {
        return delegate.findAll();
    }
}
