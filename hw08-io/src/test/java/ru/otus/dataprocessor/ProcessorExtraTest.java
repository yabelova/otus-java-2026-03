package ru.otus.dataprocessor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ProcessorExtraTest {

    @Nested
    @DisplayName("ResourcesFileLoader")
    class ResourcesFileLoaderTest {

        @Test
        @DisplayName("should throw NPE when fileName is null")
        void loaderNullFileName() {
            assertThatThrownBy(() -> new ResourcesFileLoader(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("fileName must not be null");
        }

        @Test
        @DisplayName("should throw FileProcessException when file not found")
        void loadFileNotFound() {
            var loader = new ResourcesFileLoader("nonexistent.json");
            assertThatThrownBy(() -> loader.load())
                    .isInstanceOf(FileProcessException.class)
                    .hasMessage("File not found: nonexistent.json");
        }

        @Test
        @DisplayName("should throw FileProcessException when JSON is malformed")
        void loadBrokenJson() {
            var loader = new ResourcesFileLoader("broken.json");
            assertThatThrownBy(() -> loader.load())
                    .isInstanceOf(FileProcessException.class)
                    .hasMessage("Failed to load file: broken.json")
                    .cause().isInstanceOf(IOException.class);
        }

        @Test
        @DisplayName("should return empty list when JSON array is empty")
        void loadEmptyJson() {
            var loader = new ResourcesFileLoader("empty.json");
            var result = loader.load();
            assertThat(result).hasSize(0);
        }
    }

    @Nested
    @DisplayName("ProcessorAggregator")
    class ProcessorAggregatorTest {

        @Test
        @DisplayName("should throw NPE when data is null")
        void processNullInput() {
            var processor = new ProcessorAggregator();
            assertThatThrownBy(() -> processor.process(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("data must not be null");
        }

        @Test
        @DisplayName("should return empty map when list is empty")
        void processEmptyList() {
            var processor = new ProcessorAggregator();
            assertThat(processor.process(List.of())).isEmpty();
        }

        @Test
        @DisplayName("should return sorted keys when data is provided")
        void processKeyOrder() {
            var processor = new ProcessorAggregator();
            var data = List.of(
                    new Measurement("val3", 1.0),
                    new Measurement("val1", 2.0),
                    new Measurement("val2", 3.0)
            );
            var result = processor.process(data);
            assertThat(result.keySet()).containsExactly("val1", "val2", "val3");
        }
    }

    @Nested
    @DisplayName("FileSerializer")
    class FileSerializerTest {

        @Test
        @DisplayName("should throw NPE when fileName is null")
        void serializerNullFileName() {
            assertThatThrownBy(() -> new FileSerializer(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("fileName must not be null");
        }

        @Test
        @DisplayName("should throw NPE when data is null")
        void serializeNullData() {
            var serializer = new FileSerializer("any.json");
            assertThatThrownBy(() -> serializer.serialize(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("data must not be null");
        }

        @Test
        @DisplayName("should throw FileProcessException when path is invalid")
        void serializeInvalidPath() {
            var serializer = new FileSerializer(":\0invalid/path");
            assertThatThrownBy(() -> serializer.serialize(Map.of("x", 1.0)))
                    .isInstanceOf(FileProcessException.class)
                    .hasMessage("Failed to write file: :\0invalid/path");
        }
    }
}
