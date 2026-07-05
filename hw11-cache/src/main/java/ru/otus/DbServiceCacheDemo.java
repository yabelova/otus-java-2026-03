package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.DriverManagerDataSource;
import ru.otus.model.Client;
import ru.otus.service.ClientServiceFactory;
import ru.otus.service.DBServiceClient;
import ru.otus.service.DbServiceClientCachedImpl;

import javax.sql.DataSource;

public class DbServiceCacheDemo {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(DbServiceCacheDemo.class);

    public static void main(String[] args) {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD, "hw11");
        flywayMigrations(dataSource);

        DBServiceClient rawService = ClientServiceFactory.createRawService(dataSource);
        DBServiceClient cachedService = new DbServiceClientCachedImpl(rawService);

        var saved = rawService.saveClient(new Client("demo"));
        long id = saved.getId();
        timed("Demo raw", () -> rawService.getClient(id));
        timed("Demo cached, miss", () -> cachedService.getClient(id));
        timed("Demo cached, hit", () -> cachedService.getClient(id));
    }

    private static void timed(String label, java.util.function.Supplier<?> command) {
        long start = System.nanoTime();
        var result = command.get();
        double ms = (System.nanoTime() - start) / 1_000_000.0;
        log.info("{} ({} ms): {}", label, String.format("%.3f", ms), result);
    }

    static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .schemas("hw11")
                .defaultSchema("hw11")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
