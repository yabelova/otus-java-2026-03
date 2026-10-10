package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repository.ClientRepository;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ClientRepository should ")
class ClientRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ClientRepository repository;

    @Test
    @DisplayName("save and read back a client with its address and phones")
    void shouldSaveAndFindClient() {
        Client saved = repository.save(new Client(
                "Harry Potter",
                new Address("Privet Drive"),
                Set.of(new Phone("+44 (0) 1483 559 845"), new Phone("+44 (0) 1483 559 846"))));

        assertThat(saved.getId()).isNotNull();

        var clients = repository.findAll();
        assertThat(clients).hasSize(1);

        Client client = clients.getFirst();
        assertThat(client.getId()).isEqualTo(saved.getId());
        assertThat(client.getName()).isEqualTo("Harry Potter");
        assertThat(client.getAddress().getStreet()).isEqualTo("Privet Drive");
        assertThat(client.getPhones()).extracting(Phone::getNumber)
                .containsExactlyInAnyOrder("+44 (0) 1483 559 845", "+44 (0) 1483 559 846");
    }

    @Test
    @DisplayName("store address and phones in their own tables linked by client_id")
    void shouldStorePartsInSeparateTables() {
        Client saved = repository.save(new Client(
                "Sherlock Holmes",
                new Address("Baker Street"),
                Set.of(new Phone("+44 (0) 20 7224 3688"))));

        Integer id = saved.getId();
        assertThat(count("address", id)).isEqualTo(1);
        assertThat(count("phone", id)).isEqualTo(1);
        assertThat(jdbc.queryForObject("SELECT street FROM address WHERE client_id = ?", String.class, id))
                .isEqualTo("Baker Street");
        assertThat(jdbc.queryForObject("SELECT number FROM phone WHERE client_id = ?", String.class, id))
                .isEqualTo("+44 (0) 20 7224 3688");
    }

    @Test
    @DisplayName("save a client without phones")
    void shouldSaveClientWithoutPhones() {
        Client saved = repository.save(new Client("Yoda", new Address("Dagobah"), Set.of()));

        assertThat(count("phone", saved.getId())).isZero();
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("save a client without an address and store no address row")
    void shouldSaveClientWithoutAddress() {
        Client saved = repository.save(new Client("R2-D2", null, null));

        assertThat(saved.getId()).isNotNull();
        assertThat(count("address", saved.getId())).isZero();
        assertThat(count("phone", saved.getId())).isZero();

        var clients = repository.findAll();
        assertThat(clients).hasSize(1);
        assertThat(clients.getFirst().getAddress()).isNull();
    }

    private int count(String table, Integer clientId) {
        return jdbc.queryForObject("SELECT count(*) FROM " + table + " WHERE client_id = ?", Integer.class, clientId);
    }
}
