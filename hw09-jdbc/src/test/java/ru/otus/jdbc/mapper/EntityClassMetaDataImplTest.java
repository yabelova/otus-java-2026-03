package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.annotation.Id;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EntityClassMetaDataImplTest {

    @Test
    @DisplayName("should extract metadata")
    void shouldExtractMetadata() {
        var metaData = new EntityClassMetaDataImpl<>(Titan.class);
        assertThat(metaData.getName()).isEqualTo("titan");
        assertThat(metaData.getIdField().getName()).isEqualTo("id");
        assertThat(metaData.getIdField().getType()).isEqualTo(int.class);
        assertThat(namesOf(metaData.getAllFields())).containsExactly("currentHost", "heightMeters", "id", "isShifter", "name");
        assertThat(namesOf(metaData.getFieldsWithoutId())).containsExactly("currentHost", "heightMeters", "isShifter", "name");
    }

    @Test
    @DisplayName("should throw when no Id annotation")
    void shouldThrowWhenNoIdAnnotation() {
        assertThatThrownBy(() -> new EntityClassMetaDataImpl<>(NoIdEntity.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("@Id");
    }

    @Test
    @DisplayName("should throw when no default constructor")
    void shouldThrowWhenNoDefaultConstructor() {
        assertThatThrownBy(() -> new EntityClassMetaDataImpl<>(NoDefaultCtorEntity.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("default constructor");
    }

    @Test
    @DisplayName("should return default constructor")
    void shouldReturnDefaultConstructor() throws Exception {
        var metaData = new EntityClassMetaDataImpl<>(Titan.class);
        var entity = metaData.getConstructor().newInstance();
        assertThat(entity).isNotNull().isInstanceOf(Titan.class);
    }

    @Test
    @DisplayName("id field should be annotated with Id")
    void idFieldShouldBeAnnotatedWithId() {
        var metaData = new EntityClassMetaDataImpl<>(Titan.class);
        assertThat(metaData.getIdField().isAnnotationPresent(Id.class)).isTrue();
    }

    private static List<String> namesOf(List<Field> fields) {
        return fields.stream().map(Field::getName).collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    static class NoIdEntity {
        public String name;

        public NoIdEntity() {
        }
    }

    @SuppressWarnings("unused")
    static class NoDefaultCtorEntity {
        @Id
        public int id;

        public NoDefaultCtorEntity(int id) {
            this.id = id;
        }
    }
}
