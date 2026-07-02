package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.List;

import static ru.otus.util.HibernateUtils.*;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try (var sf = buildSessionFactory(Client.class, Address.class, Phone.class)) {
            var client = new Client("Michael Scott",
                    new Address("1725 Slough Avenue, Scranton, PA 18505, USA"),
                    List.of(new Phone("+1 570-963-7312")));
            client.getAddress().setClient(client);
            client.getPhones().forEach(p -> p.setClient(client));

            doInSessionWithTransaction(sf, session -> session.persist(client));
            log.info("Saved client: id={}", client.getId());

            doInSessionWithTransaction(sf, session -> {
                var loaded = session.find(Client.class, client.getId());
                log.info("Loaded client: {}", loaded);
            });
        }
    }
}
