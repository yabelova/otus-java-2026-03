/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L18-jdbc)
 * Adapted for the hw09-jdbc module structure.
 */
package ru.otus.jdbc.mapper;

public interface EntitySQLMetaData {
    String getSelectAllSql();

    String getSelectByIdSql();

    String getInsertSql();

    String getUpdateSql();
}
