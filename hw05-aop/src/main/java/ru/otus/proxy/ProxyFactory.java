package ru.otus.proxy;

import ru.otus.model.Loggable;
import ru.otus.model.LoggableImpl;

import java.lang.reflect.Proxy;

public class ProxyFactory {
    public static Loggable createLoggable() {
        return (Loggable) Proxy.newProxyInstance(
                ProxyFactory.class.getClassLoader(),
                new Class<?>[]{Loggable.class},
                new LogInvocationHandler(new LoggableImpl())
        );
    }
}
