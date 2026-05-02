package ru.otus.framework.engine;

import ru.otus.framework.exceptions.TestException;

public class Assertions {
    /**
     * Asserts that a provided condition is true.
     */
    public static void assertTrue(boolean condition, String message) throws TestException {
        if (!condition) {
            throw new TestException(message);
        }
    }

    /**
     * Asserts that the provided code block throws a specific exception.
     */
    public static void assertThrows(Class<? extends Throwable> expectedException, Runnable code) {
        try {
            code.run();
        } catch (Throwable actualException) {
            if (expectedException == actualException.getClass()) {
                return;
            }
            throw new TestException("Expected " + expectedException.getSimpleName() +
                    " but caught " + actualException.getClass().getSimpleName());
        }
        throw new TestException("Expected " + expectedException.getSimpleName() + " was not thrown");
    }
}