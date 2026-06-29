package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntitySQLMetaDataImplTest {

    @Test
    @DisplayName("should generate sql")
    void shouldGenerateSql() {
        var entityMetaData = new EntityClassMetaDataImpl<>(Titan.class);
        var sqlMetaData = new EntitySQLMetaDataImpl(entityMetaData);

        assertThat(sqlMetaData.getSelectAllSql()).isEqualTo("select currentHost, heightMeters, id, isShifter, name from titan");
        assertThat(sqlMetaData.getSelectByIdSql()).isEqualTo("select currentHost, heightMeters, id, isShifter, name from titan where id = ?");
        assertThat(sqlMetaData.getInsertSql()).isEqualTo("insert into titan(currentHost, heightMeters, isShifter, name) values (?, ?, ?, ?)");
        assertThat(sqlMetaData.getUpdateSql()).isEqualTo("update titan set currentHost = ?, heightMeters = ?, isShifter = ?, name = ? where id = ?");
    }
}
