package ru.otus;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CountWordsTest implements TestData {

    @Test
    void should_correctly_calculate_frequencies() {
        String[] source = getWordsArray();

        Map<String, Integer> result = CollectionMethods.countWords(source);
        assertAll(
                () -> assertEquals(6, result.size(), "The number of unique words (map size) should be 6"),
                () -> assertEquals(6, result.get("Calculon"), "Calculon should appear 6 times"),
                () -> assertEquals(1, result.get("Wedding"), "Wedding should appear once")
        );
    }

    @Test
    void should_return_empty_map_for_empty_array() {
        String[] source = {};

        Map<String, Integer> result = CollectionMethods.countWords(source);
        assertTrue(result.isEmpty());
    }

    @Test
    void should_count_array_with_null_element() {
        String[] source = {"one", "two", null, null};

        Map<String, Integer> result = CollectionMethods.countWords(source);
        assertAll(
                () -> assertEquals(3, result.size(), "The number of unique words (map size) should be 3"),
                () -> assertEquals(2, result.get(null), "Null should appear 2 times")
        );
    }

    @Test
    void should_throw_npe_for_null_array() {
        assertThrows(NullPointerException.class, () -> {
            CollectionMethods.countWords(null);
        });
    }
}
