package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TestNameTest {
    @Test
    @DisplayName("Test name 1 by DisplayName")
    public void testName1() {

    }

    @Test
    @DisplayName("Test name 2 is override")
    public void test_name_2_by_Generation() {

    }

    @Test
    public void test_name_3_by_Generation() {

    }
}
