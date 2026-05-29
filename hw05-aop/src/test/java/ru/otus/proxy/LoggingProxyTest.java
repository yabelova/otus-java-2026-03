package ru.otus.proxy;

import org.junit.jupiter.api.Test;
import ru.otus.BaseStdoutTest;
import ru.otus.model.Loggable;

import static org.junit.jupiter.api.Assertions.*;

class LoggingProxyTest extends BaseStdoutTest {

    @Test
    void fullChainShouldLogAnnotatedMethod() {
        Loggable cafe = ProxyFactory.createLoggable();
        cafe.brewCoffee(5);

        assertTrue(out.toString().contains("[proxy] executed method: brewCoffee, params: 5"));
    }
}
