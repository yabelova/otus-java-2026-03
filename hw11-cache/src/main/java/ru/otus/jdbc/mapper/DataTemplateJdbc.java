/*
 * Source: Module hw09-jdbc
 */
package ru.otus.jdbc.mapper;

import ru.otus.jdbc.DataTemplate;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.exception.DataTemplateException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                return rs.next() ? createInstance(rs) : null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            List<T> resultList = new ArrayList<T>();
            try {
                while (rs.next())
                    resultList.add(createInstance(rs));
                return resultList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T instance) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getFieldValuesWithoutId(instance));
    }

    @Override
    public void update(Connection connection, T instance) {
        var params = getFieldValuesWithoutId(instance);
        params.add(getIdValue(instance));
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
    }

    @SuppressWarnings("unchecked")
    private T createInstance(ResultSet rs) {
        try {
            T instance = entityClassMetaData.getConstructor().newInstance();
            for (Field field : entityClassMetaData.getAllFields()) {
                field.set(instance, rs.getObject(field.getName(), field.getType()));
            }
            return instance;
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private Object getIdValue(T instance) {
        try {
            return entityClassMetaData.getIdField().get(instance);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getFieldValuesWithoutId(T instance) {
        var values = new ArrayList<>();
        for (var field : entityClassMetaData.getFieldsWithoutId()) {
            try {
                values.add(field.get(instance));
            } catch (IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }
        return values;
    }
}
