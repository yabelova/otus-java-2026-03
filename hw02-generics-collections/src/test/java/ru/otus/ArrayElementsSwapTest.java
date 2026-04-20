package ru.otus;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ArrayElementsSwapTest implements TestData {

    @Test
    void should_swap_integers() {
        Integer[] source = getIntArray();
        Integer[] expected = new Integer[]{13, 11, 17};

        assertSwapWorks(source, 0, 1, expected);
    }

    @Test
    void should_swap_strings() {
        String[] source = getStringArray();
        String[] expected = new String[]{"fu", "tu", "ma", "ra"};

        assertSwapWorks(source, 3, 2, expected);
    }

    @Test
    void should_swap_records() {
        PlanetExpressCrew[] source = getCrewArray();
        PlanetExpressCrew leela = source[0];
        PlanetExpressCrew fry = source[1];
        PlanetExpressCrew bender = source[2];
        PlanetExpressCrew[] expected = new PlanetExpressCrew[]{bender, fry, leela};

        assertSwapWorks(source, 0, 2, expected);
    }

    @Test
    void should_do_nothing_when_indexes_are_the_same() {
        Integer[] source = getIntArray();
        Integer[] expected = getIntArray();

        assertSwapWorks(source, 1, 1, expected);
    }

    @Test
    void should_throw_for_invalid_index1() {
        Integer[] source = getIntArray();

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            CollectionMethods.arrayElementsSwap(source, -100, 1);
        });
    }

    @Test
    void should_throw_for_invalid_index2() {
        Integer[] source = getIntArray();

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            CollectionMethods.arrayElementsSwap(source, 0, 100);
        });
    }

    @Test
    void should_throw_for_empty_array() {
        Integer[] source = {};

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            CollectionMethods.arrayElementsSwap(source, 0, 100);
        });
    }

    @Test
    void should_throw_npe_for_null_array() {
        assertThrows(NullPointerException.class, () -> {
            CollectionMethods.arrayElementsSwap(null, 0, 100);
        });
    }

    private <T> void assertSwapWorks(T[] source, int index1, int index2, T[] expected) {
        CollectionMethods.arrayElementsSwap(source, index1, index2);
        assertArrayEquals(expected, source);
    }
}