package ru.otus.jdbc.mapper;

import ru.otus.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    @SuppressWarnings("unchecked")
    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        try {
            this.constructor = clazz.getDeclaredConstructor();
            this.constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No default constructor for " + clazz.getName(), e);
        }

        this.allFields = Arrays.stream(clazz.getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());

        this.idField = allFields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No field annotated with @Id in " + clazz.getName()));

        this.fieldsWithoutId = allFields.stream()
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
