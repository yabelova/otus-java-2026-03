package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        var tableName = entityClassMetaData.getName();
        var idField = entityClassMetaData.getIdField();
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var allFields = entityClassMetaData.getAllFields();

        var allColumns = allFields.stream().map(Field::getName).collect(Collectors.joining(", "));
        var columnsWithoutId = fieldsWithoutId.stream().map(Field::getName).collect(Collectors.joining(", "));
        var placeholders = fieldsWithoutId.stream().map(f -> "?").collect(Collectors.joining(", "));
        var setClause = fieldsWithoutId.stream()
                .map(f -> f.getName() + " = ?")
                .collect(Collectors.joining(", "));

        this.selectAllSql = "select " + allColumns + " from " + tableName;
        this.selectByIdSql = "select " + allColumns + " from " + tableName + " where " + idField.getName() + " = ?";
        this.insertSql = "insert into " + tableName + "(" + columnsWithoutId + ") values (" + placeholders + ")";
        this.updateSql = "update " + tableName + " set " + setClause + " where " + idField.getName() + " = ?";
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }
}
