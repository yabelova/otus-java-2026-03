package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationStructureTest {

    @Test
    @DisplayName("Main class should contain a valid entry point")
    void mainClassShouldContainEntryPoint() throws Exception {
        Method method = Main.class.getDeclaredMethod("main", String[].class);

        int modifiers = method.getModifiers();

        assertAll("Main method contract validation",
                () -> assertTrue(Modifier.isPublic(modifiers),
                        "The main method must be public"),

                () -> assertTrue(Modifier.isStatic(modifiers),
                        "The main method must be static"),

                () -> assertEquals(void.class, method.getReturnType(),
                        "The main method must return void")
        );
    }
}
