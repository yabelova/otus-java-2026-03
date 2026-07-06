package ru.otus;

import org.flywaydb.core.Flyway;
import org.openjdk.jmh.annotations.*;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.otus.jdbc.DriverManagerDataSource;
import ru.otus.model.Client;
import ru.otus.service.ClientServiceFactory;
import ru.otus.service.DbServiceClientCachedImpl;
import ru.otus.service.DBServiceClient;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class CacheBenchmark {

    private PostgreSQLContainer<?> container;
    private DriverManagerDataSource dataSource;
    private DBServiceClient rawService;
    private DBServiceClient cachedService;
    private final long clientId = 1L;

    @Setup
    public void setup() {
        container = new PostgreSQLContainer<>("postgres:18")
                .withDatabaseName("demoDB")
                .withUsername("usr")
                .withPassword("pwd");
        container.start();

        dataSource = new DriverManagerDataSource(
                container.getJdbcUrl(), container.getUsername(), container.getPassword(), "hw11");

        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/benchmark-migration")
                .schemas("hw11")
                .defaultSchema("hw11")
                .load()
                .migrate();

        rawService = ClientServiceFactory.createRawService(dataSource);
        cachedService = new DbServiceClientCachedImpl(rawService);
    }

    @TearDown
    public void tearDown() {
        if (dataSource != null)
            dataSource.close();
        if (container != null)
            container.stop();
    }

    @Benchmark
    public Optional<Client> withoutCache() {
        return rawService.getClient(clientId);
    }

    @Benchmark
    public Optional<Client> withCache() {
        return cachedService.getClient(clientId);
    }
}
