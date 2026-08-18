package ru.otus.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import ru.otus.model.Client;

import java.util.List;

import static ru.otus.util.HibernateUtils.doInSessionWithTransaction;

@Slf4j
@RequiredArgsConstructor
public class ClientDaoHibernate implements ClientDao {

    private final SessionFactory sf;

    @Override
    public List<Client> findAll() {
        return doInSessionWithTransaction(sf, session -> {
            var entityGraph = session.getEntityGraph("client-with-all");
            return session.createQuery("from Client c", Client.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraph)
                    .getResultList();
        });
    }

    @Override
    public Client save(Client client) {
        doInSessionWithTransaction(sf, session -> {
            session.persist(client);
            return null;
        });
        return client;
    }
}
