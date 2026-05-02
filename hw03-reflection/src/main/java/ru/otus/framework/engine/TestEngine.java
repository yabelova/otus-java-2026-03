package ru.otus.framework.engine;

import ru.otus.framework.annotations.After;
import ru.otus.framework.annotations.Before;
import ru.otus.framework.annotations.Test;
import ru.otus.framework.models.MethodNamed;
import ru.otus.framework.models.MethodOrdered;
import ru.otus.framework.models.TestResult;
import ru.otus.framework.models.TestSuite;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Main engine of the test framework.
 * Responsible for discovering methods with annotations and managing their lifecycle.
 */
public class TestEngine {

    /**
     * Runs all tests in the specified class.
     * Steps: Before -> Test -> After for each @Test method.
     */
    public static void run(String className) {
        if (className == null || className.isEmpty()) {
            System.err.println("ClassName is null or empty");
            return;
        }
        try {
            Class<?> cl = Class.forName(className);
            TestResult result = runTestClass(cl);
            printReport(result);
        } catch (ClassNotFoundException e) {
            System.err.println("Critical error: Class '" + className + "' not found ");
        }
    }

    /**
     * Executes tests for a given Class object and returns the results.
     */
    public static TestResult runTestClass(Class<?> cl) {
        TestResult result = new TestResult();
        long startTime = System.currentTimeMillis();

        TestSuite suite = getTestSuite(cl);

        for (MethodNamed test : suite.testMethods()) {
            runSingleTest(cl, test, suite.beforeMethods(), suite.afterMethods(), result);
        }

        result.setExecutionTime(System.currentTimeMillis() - startTime);
        return result;
    }

    private static TestSuite getTestSuite(Class<?> cl) {
        List<MethodOrdered> beforeMethods = new ArrayList<>();
        List<MethodOrdered> afterMethods = new ArrayList<>();
        List<MethodNamed> testMethods = new ArrayList<>();

        for (Method method : cl.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                method.setAccessible(true);
                int order = method.getAnnotation(Before.class).order();
                beforeMethods.add(new MethodOrdered(method, order));

            } else if (method.isAnnotationPresent(After.class)) {
                method.setAccessible(true);
                int order = method.getAnnotation(After.class).order();
                afterMethods.add(new MethodOrdered(method, order));

            } else if (method.isAnnotationPresent(Test.class)) {
                method.setAccessible(true);
                Test anno = method.getAnnotation(Test.class);
                String name = anno.displayName().isEmpty() ? method.getName() : anno.displayName();
                testMethods.add(new MethodNamed(method, name));
            }
        }

        beforeMethods.sort(Comparator.comparingInt(MethodOrdered::order));
        afterMethods.sort(Comparator.comparingInt(MethodOrdered::order));

        return new TestSuite(beforeMethods, testMethods, afterMethods);
    }

    private static void runSingleTest(Class<?> cl,
                                      MethodNamed testMethod,
                                      List<MethodOrdered> beforeMethods,
                                      List<MethodOrdered> afterMethods,
                                      TestResult result) {
        Object testInstance = null;
        try {
            var constructor = cl.getDeclaredConstructor();
            constructor.setAccessible(true);
            testInstance = constructor.newInstance();

            for (MethodOrdered before : beforeMethods) {
                before.method().invoke(testInstance);
            }

            testMethod.method().invoke(testInstance);

            result.addPassed(testMethod.name());
        } catch (Exception e) {
            result.addFailed(testMethod.name(), e.getCause());
        } finally {
            if (testInstance != null) {
                for (MethodOrdered after : afterMethods) {
                    try {
                        after.method().invoke(testInstance);
                    } catch (Exception e) {
                        Throwable cause = e.getCause();
                        String message = (cause != null) ? cause.toString() : e.getMessage();
                        System.err.println("Ошибка в After методе: " + message);
                    }
                }
            }
        }
    }

    private static void printReport(TestResult result) {
        System.out.println(result.getStatistics());
    }
}