package ru.otus;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class TestParameterizedTest {

    static Stream<Arguments> sumDataProvider() {
        return Stream.of(
                arguments(2, 2, 4),
                arguments(5, 10, 15),
                arguments(-1, 1, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("sumDataProvider")
    void testAddition(int a, int b, int expectedSum) {
        assertEquals(expectedSum, a + b);
    }
}