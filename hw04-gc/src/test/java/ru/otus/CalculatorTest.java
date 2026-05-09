package ru.otus;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 100, 100_000, 1_000_000})
    void optimizedShouldWorkAsOriginal(int iterations) {

        var originalSummator = new ru.otus.calculator.original.Summator();
        var optimizedSummator = new ru.otus.calculator.optimized.Summator();

        for (int i = 0; i < iterations; i++) {
            originalSummator.calc(new ru.otus.calculator.original.Data(i));
        }

        var data = new ru.otus.calculator.optimized.Data(0);
        for (int i = 0; i < iterations; i++) {
            data.setValue(i);
            optimizedSummator.calc(data);
        }

        assertAll("Results must be identical for " + iterations + " iterations",
                () -> assertEquals(originalSummator.getPrevValue(), optimizedSummator.getPrevValue(), "PrevValue mismatch"),
                () -> assertEquals(originalSummator.getPrevPrevValue(), optimizedSummator.getPrevPrevValue(), "PrevPrevValue mismatch"),
                () -> assertEquals(originalSummator.getSumLastThreeValues(), optimizedSummator.getSumLastThreeValues(), "SumLastThreeValues mismatch"),
                () -> assertEquals(originalSummator.getSomeValue(), optimizedSummator.getSomeValue(), "SomeValue mismatch"),
                () -> assertEquals(originalSummator.getSum(), optimizedSummator.getSum(), "Sum mismatch")
        );
    }
}