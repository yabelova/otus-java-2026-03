package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

public class FileSerializer implements Serializer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = Objects.requireNonNull(fileName, "fileName must not be null");
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        Objects.requireNonNull(data, "data must not be null");
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            mapper.writeValue(outputStream, data);
        } catch (IOException e) {
            throw new FileProcessException("Failed to write file: " + fileName, e);
        }
    }
}
