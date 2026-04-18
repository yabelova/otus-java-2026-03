package ru.otus;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GreetingServiceTest {

    private final GreetingService gs = new GreetingService();

    @Test
    public void should_have_passed_regular_name() {
        String actual = gs.greet("Jon Show");

        assertNotNull(actual);
        assertTrue(actual.contains("Jon Show"));
    }

    @Test
    public void should_support_cyrillic_names() {
        String actual = gs.greet("Арья Старк");

        assertNotNull(actual);
        assertTrue(actual.contains("Арья Старк"));
    }

    @Test
    void should_trim_name() {
        String actual = gs.greet("  Tyrion Lannister  ");

        assertFalse(actual.contains("  Tyrion Lannister  "));
    }

    @Test
    void should_handle_blank_name() {
        assertDoesNotThrow(() -> gs.greet(""),
                "Should not failed with empty name");

        String actual = gs.greet("");
        assertNotNull(actual, "Should have greet for empty name");
    }

    @Test
    void should_handle_null_name() {
        assertDoesNotThrow(() -> gs.greet(null),
                "Should not failed with null name");

        String actual = gs.greet(null);
        assertNotNull(actual, "Should have greet for null name");
    }
}
