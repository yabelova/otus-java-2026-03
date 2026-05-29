package ru.otus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method for logging via JDK dynamic proxy.
 * Processed by {@link ru.otus.proxy.LogInvocationHandler} at invocation time.
 * Only effective when the object is created through {@link ru.otus.proxy.ProxyFactory}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogByProxy {
}
