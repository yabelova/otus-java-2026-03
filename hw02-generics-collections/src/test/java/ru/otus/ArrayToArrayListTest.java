package ru.otus;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ArrayToArrayListTest implements TestData {

    @Test
    void should_work_with_integers() {
        Integer[] source = getIntArray();
        assertConversionCorrect(source);
    }

    @Test
    void should_work_with_strings() {
        String[] source = getStringArray();
        assertConversionCorrect(source);
    }

    @Test
    void should_work_with_records() {
        PlanetExpressCrew[] source = getCrewArray();
        assertConversionCorrect(source);
    }

    @Test
    void should_return_empty_list_for_empty_array() {
        String[] source = {};
        ArrayList<String> result = CollectionMethods.arrayToArrayList(source);
        assertTrue(result.isEmpty());
    }

    @Test
    void should_throw_npe_for_null_array() {
        assertThrows(NullPointerException.class, () -> {
            CollectionMethods.arrayToArrayList(null);
        });
    }

    @Test
    void resulting_list_should_be_modifiable() {
        String[] source = getStringArray();

        ArrayList<String> result = CollectionMethods.arrayToArrayList(source);
        assertAll(
                () -> assertDoesNotThrow(() -> result.add("!!"), "Resulting list should be modifiable"),
                () -> assertEquals(5, result.size(), "Resulting list should have size 5")
        );
    }

    private <T> void assertConversionCorrect(T[] source) {
        ArrayList<T> result = CollectionMethods.arrayToArrayList(source);
        assertAll(
                () -> assertInstanceOf(ArrayList.class, result, "Type should be ArrayList"),
                () -> assertEquals(source.length, result.size(), "Size of source and result should be the same")
        );
        for (int i = 0; i < source.length; i++) {
            assertSame(source[i], result.get(i), "Element at index " + i + " should be the same in source and result");
        }
    }
}
