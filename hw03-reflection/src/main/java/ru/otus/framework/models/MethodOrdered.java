package ru.otus.framework.models;

import java.lang.reflect.Method;

/**
 * Wrapper for a method with its execution order.
 */
public record MethodOrdered(Method method, int order) {
}