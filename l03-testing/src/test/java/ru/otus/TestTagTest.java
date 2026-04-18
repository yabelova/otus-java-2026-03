package ru.otus;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TestTagTest {
    @Test
    @Tag("smoke")
    public void testSmoke1() {

    }

    @Test
    @Tag("smoke")
    public void testSmoke2() {

    }

    @Test
    @Tag("smoke")
    public void testSmoke3() {

    }

    @Test
    @Tag("smoke")
    public void testSmoke4() {

    }

    @Test
    @Tag("regression")
    public void testRegression1() {

    }

    @Test
    @Tag("regression")
    public void testRegression2() {

    }

    @Test
    @Tag("smoke")
    @Tag("regression")
    public void testSmokeAndRegression() {

    }
}