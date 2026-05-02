package ru.otus.framework.models;

import java.lang.reflect.Method;

/**
 * Wrapper for a test method with its display name.
 */
public record MethodNamed(Method method, String name) {
}