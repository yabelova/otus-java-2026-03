package ru.otus.service;

import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.TransactionRunnerJdbc;
import ru.otus.jdbc.mapper.DataTemplateJdbc;
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.EntitySQLMetaDataImpl;
import ru.otus.model.Client;

import javax.sql.DataSource;

public class ClientServiceFactory {

    public static DbServiceClientImpl createRawService(DataSource dataSource) {
        var tr = new TransactionRunnerJdbc(dataSource);
        var exec = new DbExecutorImpl();
        var meta = new EntityClassMetaDataImpl<>(Client.class);
        var sql = new EntitySQLMetaDataImpl(meta);
        var dt = new DataTemplateJdbc<>(exec, sql, meta);
        return new DbServiceClientImpl(tr, dt);
    }
}
