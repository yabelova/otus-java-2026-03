package ru.otus.proxy;

import ru.otus.model.Loggable;

import java.lang.reflect.Proxy;

public class ProxyFactory {
    /**
     * Creates a {@link Loggable} proxy that logs methods annotated with
     * {@link ru.otus.annotation.Log @Log}.
     */
    public static Loggable create(Loggable target) {
        return (Loggable) Proxy.newProxyInstance(
                ProxyFactory.class.getClassLoader(),
                new Class<?>[]{Loggable.class},
                new LogInvocationHandler(target)
        );
    }
}
