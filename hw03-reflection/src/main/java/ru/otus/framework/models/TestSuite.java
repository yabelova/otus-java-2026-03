package ru.otus.framework.models;

import java.util.List;

/**
 * Container for collected test methods grouped by their lifecycle type.
 */
public record TestSuite(
        List<MethodOrdered> beforeMethods,
        List<MethodNamed> testMethods,
        List<MethodOrdered> afterMethods
) {
}