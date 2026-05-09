import org.junit.jupiter.api.Test;
import ru.otus.framework.engine.TestEngine;
import ru.otus.framework.models.TestResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FrameworkEngineTest {

    @Test
    void shouldCorrectlyCalculateStatistics() {
        TestResult result = TestEngine.runTestClass(MockTest.class);

        assertEquals(3, result.getTotalCount(), "Total count should be 3");
        assertEquals(2, result.getPassedCount(), "Passed count should be 2");
        assertEquals(1, result.getFailedCount(), "Failed count should be 1");
    }

    @Test
    void shouldExecuteMethodsInCorrectOrder() {
        List<String> expected = List.of(
                "before0", "before1", "before2", "test", "after0", "after1",
                "before0", "before1", "before2", "test", "after0", "after1",
                "before0", "before1", "before2", "test", "after0", "after1");
        MockTest.runLog.clear();

        TestEngine.runTestClass(MockTest.class);

        assertEquals(expected, MockTest.runLog);
    }

    @Test
    void shouldUseDisplayNameWhenProvided() {
        TestResult result = TestEngine.runTestClass(MockTest.class);

        List<String> passedNames = result.getPassedTests();
        assertTrue(passedNames.contains("Test with provided displayName"), "Should use displayName when provided");
        assertTrue(passedNames.contains("shouldPassWithoutDisplayName"), "Should use method name when displayName is empty");
    }

    @Test
    void shouldCreateNewInstanceForEachTest() {
        MockTest.instancesCreated = 0;

        TestEngine.runTestClass(MockTest.class);

        assertEquals(3, MockTest.instancesCreated);
    }

    @Test
    void shouldReturnEmptyResultForNonTestClass() {
        TestResult result = TestEngine.runTestClass(String.class);

        assertEquals(0, result.getTotalCount());
    }
}