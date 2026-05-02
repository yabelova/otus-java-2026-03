import org.junit.jupiter.api.Test;
import ru.otus.framework.exceptions.TestException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FrameworkAssertionTest {

    @Test
    void assertTrueShouldPassWhenConditionIsTrue() {
        assertDoesNotThrow(() ->
                ru.otus.framework.engine.Assertions.assertTrue(true, "Error message")
        );
    }

    @Test
    void assertTrueShouldThrowExceptionWhenConditionIsFalse() {
        assertThrows(TestException.class, () ->
                ru.otus.framework.engine.Assertions.assertTrue(false, "Error message")
        );
    }

    @Test
    void assertThrowsShouldPassWhenCorrectExceptionIsThrown() {
        assertDoesNotThrow(() ->
                ru.otus.framework.engine.Assertions.assertThrows(NullPointerException.class, () -> {
                    throw new NullPointerException("Expected");
                })
        );
    }

    @Test
    void assertThrowsShouldFailWhenWrongExceptionIsThrown() {
        assertThrows(TestException.class, () ->
                ru.otus.framework.engine.Assertions.assertThrows(NullPointerException.class, () -> {
                    throw new RuntimeException("Wrong exception");
                })
        );
    }
}