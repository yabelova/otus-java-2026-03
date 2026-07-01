/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L18-jdbc)
 * Adapted for the hw09-jdbc module structure.
 */
package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public interface EntityClassMetaData<T> {
    String getName();

    Constructor<T> getConstructor();

    Field getIdField();

    List<Field> getAllFields();

    List<Field> getFieldsWithoutId();
}
