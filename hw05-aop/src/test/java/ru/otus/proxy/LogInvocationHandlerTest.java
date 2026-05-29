package ru.otus.proxy;

import org.junit.jupiter.api.Test;
import ru.otus.BaseStdoutTest;
import ru.otus.model.LoggableImpl;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class LogInvocationHandlerTest extends BaseStdoutTest {

    private final LogInvocationHandler handler = new LogInvocationHandler(new LoggableImpl());

    @Test
    void shouldLogWhenMethodHasLogByProxy() throws Throwable {
        Method method = LoggableImpl.class.getMethod("orderPastry", int.class, int.class, String.class);
        handler.invoke(null, method, new Object[]{101, 2, "Croissant"});

        assertTrue(out.toString().contains("[proxy] executed method: orderPastry, params: [101, 2, Croissant]"));
    }

    @Test
    void shouldNotLogWhenMethodHasNoLogByProxy() throws Throwable {
        Method method = LoggableImpl.class.getMethod("prepareTable", int.class, int.class);
        handler.invoke(null, method, new Object[]{2, 3});

        assertFalse(out.toString().contains("[proxy] executed method: prepareTable"));
    }

    @Test
    void shouldLogOnlyAnnotatedOverload() throws Throwable {
        Method method = LoggableImpl.class.getMethod("orderPastry", int.class, String.class);
        handler.invoke(null, method, new Object[]{105, "Bun"});

        assertFalse(out.toString().contains("executed method: orderPastry"));
    }
}
