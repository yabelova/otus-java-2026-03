package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.jdbc.DbExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DataTemplateJdbcTest {

    @Mock
    private DbExecutor dbExecutor;

    @Mock
    private Connection connection;

    @Mock
    private ResultSet resultSet;

    private DataTemplateJdbc<Titan> template;

    @BeforeEach
    void setUp() {
        var entityMetaData = new EntityClassMetaDataImpl<>(Titan.class);
        var sqlMetaData = new EntitySQLMetaDataImpl(entityMetaData);
        template = new DataTemplateJdbc<>(dbExecutor, sqlMetaData, entityMetaData);
    }

    @Test
    @DisplayName("should map result set to entity via findById")
    void shouldFindByIdAndMapResultSetToEntity() throws SQLException {
        given(resultSet.next()).willReturn(true);
        given(resultSet.getObject("currentHost", String.class)).willReturn("Annie Leonhart");
        given(resultSet.getObject("heightMeters", int.class)).willReturn(14);
        given(resultSet.getObject("id", int.class)).willReturn(1);
        given(resultSet.getObject("isShifter", boolean.class)).willReturn(true);
        given(resultSet.getObject("name", String.class)).willReturn("Female Titan");

        given(dbExecutor.executeSelect(
                eq(connection),
                eq("select currentHost, heightMeters, id, isShifter, name from titan where id = ?"),
                eq(List.of(1L)),
                any()))
                .willAnswer(invocation -> {
                    Function<ResultSet, Titan> handler = invocation.getArgument(3);
                    return Optional.ofNullable(handler.apply(resultSet));
                });

        var entity = template.findById(connection, 1L);

        assertThat(entity).isPresent();
        assertThat(entity.get().currentHost).isEqualTo("Annie Leonhart");
        assertThat(entity.get().heightMeters).isEqualTo(14);
        assertThat(entity.get().id).isEqualTo(1);
        assertThat(entity.get().isShifter).isTrue();
        assertThat(entity.get().name).isEqualTo("Female Titan");
    }

    @Test
    @DisplayName("should return empty optional when entity not found")
    void shouldFindByIdReturnEmptyWhenNotFound() throws SQLException {
        given(resultSet.next()).willReturn(false);

        given(dbExecutor.executeSelect(
                eq(connection),
                eq("select currentHost, heightMeters, id, isShifter, name from titan where id = ?"),
                eq(List.of(999L)),
                any()))
                .willAnswer(invocation -> {
                    Function<ResultSet, Titan> handler = invocation.getArgument(3);
                    return Optional.ofNullable(handler.apply(resultSet));
                });

        var entity = template.findById(connection, 999L);

        assertThat(entity).isEmpty();
    }

    @Test
    @DisplayName("should map multiple result set rows to entity list via findAll")
    void shouldFindAllAndMapMultipleRows() throws SQLException {
        given(resultSet.next()).willReturn(true, true, false);
        given(resultSet.getObject("currentHost", String.class)).willReturn("Eren Yeager", "Reiner Braun");
        given(resultSet.getObject("heightMeters", int.class)).willReturn(15, 15);
        given(resultSet.getObject("id", int.class)).willReturn(1, 2);
        given(resultSet.getObject("isShifter", boolean.class)).willReturn(true, true);
        given(resultSet.getObject("name", String.class)).willReturn("Attack Titan", "Armored Titan");

        given(dbExecutor.executeSelect(
                eq(connection),
                eq("select currentHost, heightMeters, id, isShifter, name from titan"),
                eq(List.of()),
                any()))
                .willAnswer(invocation -> {
                    Function<ResultSet, List<Titan>> handler = invocation.getArgument(3);
                    return Optional.ofNullable(handler.apply(resultSet));
                });

        var entities = template.findAll(connection);

        assertThat(entities).hasSize(2);
        assertThat(entities.get(0).currentHost).isEqualTo("Eren Yeager");
        assertThat(entities.get(0).name).isEqualTo("Attack Titan");
        assertThat(entities.get(1).currentHost).isEqualTo("Reiner Braun");
        assertThat(entities.get(1).name).isEqualTo("Armored Titan");
    }

    @Test
    @DisplayName("should return empty list when table has no rows")
    void shouldFindAllReturnEmptyListWhenNoRows() throws SQLException {
        given(resultSet.next()).willReturn(false);

        given(dbExecutor.executeSelect(
                eq(connection),
                eq("select currentHost, heightMeters, id, isShifter, name from titan"),
                eq(List.of()),
                any()))
                .willAnswer(invocation -> {
                    @SuppressWarnings("unchecked")
                    Function<ResultSet, List<Titan>> handler = invocation.getArgument(3);
                    return Optional.ofNullable(handler.apply(resultSet));
                });

        var entities = template.findAll(connection);

        assertThat(entities).isEmpty();
    }

    @Test
    @DisplayName("should map entity with null string and false boolean fields")
    void shouldFindByIdWithNullFields() throws SQLException {
        given(resultSet.next()).willReturn(true);
        given(resultSet.getObject("currentHost", String.class)).willReturn(null);
        given(resultSet.getObject("heightMeters", int.class)).willReturn(7);
        given(resultSet.getObject("id", int.class)).willReturn(42);
        given(resultSet.getObject("isShifter", boolean.class)).willReturn(false);
        given(resultSet.getObject("name", String.class)).willReturn("Pure Titan");

        given(dbExecutor.executeSelect(
                eq(connection),
                eq("select currentHost, heightMeters, id, isShifter, name from titan where id = ?"),
                eq(List.of(42L)),
                any()))
                .willAnswer(invocation -> {
                    Function<ResultSet, Titan> handler = invocation.getArgument(3);
                    return Optional.ofNullable(handler.apply(resultSet));
                });

        var entity = template.findById(connection, 42L);

        assertThat(entity).isPresent();
        assertThat(entity.get().currentHost).isNull();
        assertThat(entity.get().heightMeters).isEqualTo(7);
        assertThat(entity.get().id).isEqualTo(42);
        assertThat(entity.get().isShifter).isFalse();
        assertThat(entity.get().name).isEqualTo("Pure Titan");
    }

    @Test
    @DisplayName("should insert entity and return generated id")
    void shouldInsertAndReturnGeneratedId() {
        given(dbExecutor.executeStatement(
                eq(connection),
                eq("insert into titan(currentHost, heightMeters, isShifter, name) values (?, ?, ?, ?)"),
                eq(List.of("Annie Leonhart", 14, true, "Female Titan"))))
                .willReturn(1L);

        var id = template.insert(connection, new Titan("Female Titan", 14, true, "Annie Leonhart"));

        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("should update entity with id parameter at the end")
    void shouldUpdateWithIdAtTheEnd() {
        var titan = new Titan("Colossal Titan", 60, true, "Bertolt Hoover");
        titan.id = 3;

        template.update(connection, titan);

        verify(dbExecutor).executeStatement(
                connection,
                "update titan set currentHost = ?, heightMeters = ?, isShifter = ?, name = ? where id = ?",
                List.of("Bertolt Hoover", 60, true, "Colossal Titan", 3));
    }
}
