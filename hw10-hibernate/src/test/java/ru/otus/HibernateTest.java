package ru.otus;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static ru.otus.util.HibernateUtils.buildSessionFactory;
import static ru.otus.util.HibernateUtils.doInSessionWithTransaction;

class HibernateTest {
    private static PostgreSQLContainer<?> container;
    private SessionFactory sf;

    @BeforeAll
    static void startContainer() {
        container = new PostgreSQLContainer<>("postgres:18")
                .withDatabaseName("demoDB")
                .withUsername("usr")
                .withPassword("pwd");
        container.start();
    }

    @AfterAll
    static void stopContainer() {
        if (container != null) container.stop();
    }

    @BeforeEach
    void setUp() {
        var props = new Properties();
        props.setProperty("hibernate.connection.url", container.getJdbcUrl());
        props.setProperty("hibernate.connection.username", container.getUsername());
        props.setProperty("hibernate.connection.password", container.getPassword());
        sf = buildSessionFactory(props, Client.class, Address.class, Phone.class);
    }

    @AfterEach
    void tearDown() {
        if (sf != null) sf.close();
    }

    @Test
    @DisplayName("Should create only 3 tables for entities")
    void shouldCreateOnlyThreeTables() {
        doInSessionWithTransaction(sf, session -> session.doWork(connection -> {
            var rs = connection.getMetaData()
                    .getTables(null, "public", "%", new String[]{"TABLE"});
            var tables = new java.util.ArrayList<String>();
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME").toLowerCase());
            }
            assertThat(tables)
                    .containsExactlyInAnyOrder("client", "address", "phone");
        }));
    }

    @Test
    @DisplayName("Should insert only")
    void shouldInsertOnly() {
        createClientWithAddressAndPhones();

        assertThat(sf.getStatistics().getEntityInsertCount()).isEqualTo(4);
        assertThat(sf.getStatistics().getEntityUpdateCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should correct fill all Client, Address, Phone parameters")
    void shouldCorrectFillAllParameters() {
        Long clientId = createClientWithAddressAndPhones();

        doInSessionWithTransaction(sf, session -> {
            Client client = session.find(Client.class, clientId);

            assertThat(client.getName()).isEqualTo(CLIENT_NAME);

            assertThat(client.getAddress().getStreet()).isEqualTo(STREET);
            assertThat(client.getAddress().getClient().getName()).isEqualTo(CLIENT_NAME);

            assertThat(client.getPhones()).hasSize(2);
            assertThat(client.getPhones())
                    .extracting(Phone::getNumber)
                    .containsExactlyInAnyOrder(PHONE_1, PHONE_2);
            assertThat(client.getPhones())
                    .extracting(p -> p.getClient().getName())
                    .containsExactlyInAnyOrder(CLIENT_NAME, CLIENT_NAME);
        });
    }

    @Test
    @DisplayName("Should cascade delete Phone and Address entities")
    void shouldCascadeDeleteChildEntities() {
        Long clientId = createClientWithAddressAndPhones();

        doInSessionWithTransaction(sf, session -> {
            session.remove(session.find(Client.class, clientId));
        });

        assertThat(sf.getStatistics().getEntityDeleteCount()).isEqualTo(4);

        doInSessionWithTransaction(sf, session -> {
            assertThat(session.createQuery("select a from Address a", Address.class).list()).isEmpty();
            assertThat(session.createQuery("select p from Phone p", Phone.class).list()).isEmpty();
        });
    }

    @Test
    @DisplayName("Should use graph with one select")
    void shouldUseGraphWithOneSelect() {
        Long clientId = createClientWithAddressAndPhones();
        sf.getStatistics().clear();
        doInSessionWithTransaction(sf, session -> {
            var graph = session.getEntityGraph("client-with-all");
            Client client = session.find(Client.class, clientId,
                    Map.of("jakarta.persistence.fetchgraph", graph));

            assertThat(client.getName()).isEqualTo(CLIENT_NAME);
            assertThat(client.getAddress().getStreet()).isEqualTo(STREET);
            assertThat(client.getPhones()).hasSize(2);
            assertThat(client.getPhones())
                    .extracting(Phone::getNumber)
                    .containsExactlyInAnyOrder(PHONE_1, PHONE_2);
        });

        assertThat(sf.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    private Long createClientWithAddressAndPhones() {
        Client client = new Client(CLIENT_NAME,
                new Address(STREET),
                listOf(new Phone(PHONE_1), new Phone(PHONE_2)));
        client.getAddress().setClient(client);
        client.getPhones().forEach(p -> p.setClient(client));

        doInSessionWithTransaction(sf, session -> session.persist(client));
        return client.getId();
    }

    private static final String CLIENT_NAME = "Tony Stark";
    private static final String STREET = "10880 Malibu Point, Malibu, CA 90265, USA";
    private static final String PHONE_1 = "+1 212-970-4133";
    private static final String PHONE_2 = "+1 800-476-6626";
}
