package ru.otus.classfileapi;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.otus.BaseStdoutTest;
import ru.otus.model.Loggable;
import ru.otus.model.LoggableImpl;

import static org.junit.jupiter.api.Assertions.*;

@Tag("agent")
class LogAgentTest extends BaseStdoutTest {

    @Test
    void shouldLogWhenMethodHasLog() {
        Loggable cafe = new LoggableImpl();
        cafe.brewCoffee(8);
        assertTrue(out.toString().contains("[agent] executed method: brewCoffee, params: [8]"));
    }

    @Test
    void shouldNotLogWhenMethodHasNoLog() {
        Loggable cafe = new LoggableImpl();
        cafe.addSugar(2, true);

        assertFalse(out.toString().contains("[agent] executed method: addSugar"));
    }

    @Test
    void shouldLogOnlyAnnotatedOverload() {
        Loggable cafe = new LoggableImpl();
        cafe.orderPastry(101, "Bun");

        assertFalse(out.toString().contains("[agent] executed method: orderPastry"));
    }
}
