package ru.otus.classfileapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import ru.otus.BaseStdoutTest;
import ru.otus.model.Loggable;
import ru.otus.model.LoggableImpl;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfSystemProperty(named = "logging.agent.active", matches = "true")
class LoggingAgentTest extends BaseStdoutTest {

    @Test
    void shouldLogWhenMethodHasLogByAgent() {
        Loggable cafe = new LoggableImpl();
        cafe.prepareTable(5, 3);

        assertTrue(out.toString().contains("[agent] executed method: prepareTable, params: 5, 3"));
    }

    @Test
    void shouldNotLogWhenMethodHasNoLogByAgent() {
        Loggable cafe = new LoggableImpl();
        cafe.brewCoffee(4);

        assertFalse(out.toString().contains("[agent] executed method: brewCoffee"));
    }

    @Test
    void shouldLogOnlyAnnotatedOverload() {
        Loggable cafe = new LoggableImpl();
        cafe.orderPastry(101, "Bun");

        assertFalse(out.toString().contains("[agent] executed method: orderPastry"));
    }
}
