package ru.otus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method for logging via Java Instrumentation agent.
 * Processed by {@link ru.otus.classfileapi.LogTransformer} at class-loading time.
 * Only effective when the JVM is started with {@code -javaagent}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogByAgent {
}
