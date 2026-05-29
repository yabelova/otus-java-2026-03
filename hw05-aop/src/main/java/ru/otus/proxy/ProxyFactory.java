package ru.otus.proxy;

import ru.otus.model.Loggable;
import ru.otus.model.LoggableImpl;

import java.lang.reflect.Proxy;

public class ProxyFactory {
    /**
     * Creates a {@link ru.otus.model.Loggable} proxy that logs methods annotated with
     * {@link ru.otus.annotation.LogByProxy @LogByProxy}.
     */
    public static Loggable createLoggable() {
        return (Loggable) Proxy.newProxyInstance(
                ProxyFactory.class.getClassLoader(),
                new Class<?>[]{Loggable.class},
                new LogInvocationHandler(new LoggableImpl())
        );
    }
}
