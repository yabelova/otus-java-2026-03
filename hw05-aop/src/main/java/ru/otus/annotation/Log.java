package ru.otus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method for logging.
 * <p>
 * Processed by:
 * <ul>
 *   <li>{@link ru.otus.proxy.LogInvocationHandler} — when called through a JDK dynamic proxy</li>
 *   <li>{@link ru.otus.classfileapi.LogTransformer} — when the JVM is started with {@code -javaagent}</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
}
