package ru.otus.framework.models;

/**
 * Represents a failed test case with its name and the cause of failure.
 */
public record TestFailure(String name, Throwable cause) {
}