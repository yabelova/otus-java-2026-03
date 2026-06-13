package ru.otus.dataprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = Objects.requireNonNull(fileName, "fileName must not be null");
    }

    @Override
    public List<Measurement> load() {
        // читает файл, парсит и возвращает результат
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new FileProcessException("File not found: " + fileName);
            }
            return mapper.readValue(inputStream, new TypeReference<List<Measurement>>() { });
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
