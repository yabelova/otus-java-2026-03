import java.util.ArrayList;
import java.util.List;

public class MockTest {
    public static final List<String> runLog = new ArrayList<>();
    static int instancesCreated = 0;

    public MockTest() {
        instancesCreated++;
    }

    @ru.otus.framework.annotations.Test
    void shouldFailTest() {
        runLog.add("test");
        throw new RuntimeException("Fail");
    }

    @ru.otus.framework.annotations.Test(displayName = "Test with provided displayName")
    void shouldPassWithDisplayName() {
        runLog.add("test");
    }

    @ru.otus.framework.annotations.Test
    void shouldPassWithoutDisplayName() {
        runLog.add("test");
    }


    @ru.otus.framework.annotations.After
    void brokenAfter0() {
        runLog.add("after0");
        throw new RuntimeException("After crash!");
    }

    @ru.otus.framework.annotations.After(order = 1)
    void after1() {
        runLog.add("after1");
    }

    @ru.otus.framework.annotations.Before
    void before0() {
        runLog.add("before0");
    }

    @ru.otus.framework.annotations.Before(order = 1)
    void before1() {
        runLog.add("before1");
    }

    @ru.otus.framework.annotations.Before(order = 2)
    void before2() {
        runLog.add("before2");
    }
}