/*
 * Source: Module hw09-jdbc
 * Changes: Added AutoCloseable + close() for HikariCP pool cleanup; added schema parameter support.
 */
package ru.otus.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DriverManagerDataSource implements DataSource, AutoCloseable {
    private DataSource dataSourcePool;

    public DriverManagerDataSource(String url, String user, String pwd) {
        this(url, user, pwd, null);
    }

    public DriverManagerDataSource(String url, String user, String pwd, String schema) {
        createConnectionPool(url, user, pwd, schema);
    }

    @Override
    public void close() {
        if (dataSourcePool instanceof HikariDataSource hikari) {
            hikari.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSourcePool.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintWriter getLogWriter() { throw new UnsupportedOperationException(); }
    @Override
    public void setLogWriter(PrintWriter out) { throw new UnsupportedOperationException(); }
    @Override
    public int getLoginTimeout() { throw new UnsupportedOperationException(); }
    @Override
    public void setLoginTimeout(int seconds) { throw new UnsupportedOperationException(); }
    @Override
    public Logger getParentLogger() { throw new UnsupportedOperationException(); }
    @Override
    public <T> T unwrap(Class<T> iface) { throw new UnsupportedOperationException(); }
    @Override
    public boolean isWrapperFor(Class<?> iface) { throw new UnsupportedOperationException(); }

    private void createConnectionPool(String url, String user, String pwd, String schema) {
        var config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setConnectionTimeout(3000);
        config.setIdleTimeout(60000);
        config.setMaxLifetime(600000);
        config.setAutoCommit(false);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setPoolName("DemoHiPool");
        config.setRegisterMbeans(true);
        if (schema != null) {
            config.setSchema(schema);
        }

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        config.setUsername(user);
        config.setPassword(pwd);

        dataSourcePool = new HikariDataSource(config);
    }
}
