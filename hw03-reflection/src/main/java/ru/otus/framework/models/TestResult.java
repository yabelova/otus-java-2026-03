package ru.otus.framework.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Accumulates the results of a test session, including statistics and execution time.
 * Provides a formatted report via the getStatistics method.
 */
public class TestResult {
    private final List<String> passedTests = new ArrayList<>();
    private final List<TestFailure> failedTests = new ArrayList<>();
    private long executionTimeMs = 0L;

    public void addPassed(String name) {
        passedTests.add(name);
    }

    public void addFailed(String name, Throwable cause) {
        failedTests.add(new TestFailure(name, cause));
    }

    public void setExecutionTime(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public List<String> getPassedTests() {
        return passedTests;
    }

    public List<TestFailure> getFailedTests() {
        return failedTests;
    }

    public int getPassedCount() {
        return passedTests.size();
    }

    public int getFailedCount() {
        return failedTests.size();
    }

    public int getTotalCount() {
        return getPassedCount() + getFailedCount();
    }

    public String getStatistics() {
        String line = "=".repeat(40) + "\n";
        StringBuilder sb = new StringBuilder();

        sb.append("\n")
                .append(line)
                .append("TEST SESSION RESULTS\n")
                .append(line);

        sb.append("PASSED TESTS:\n");
        if (passedTests.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (String name : passedTests) {
                sb.append("  [OK] ").append(name).append("\n");
            }
        }

        sb.append("\nFAILED TESTS:\n");
        if (failedTests.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (TestFailure failure : failedTests) {
                String errorMsg = (failure.cause() != null) ? failure.cause().toString() : "No message";
                sb.append("  [FAIL] ").append(failure.name()).append("\n");
                sb.append("         Reason: ").append(errorMsg).append("\n");
            }
        }

        sb.append(line);
        sb.append(String.format("TOTAL: %d | PASSED: %d | FAILED: %d%n",
                getTotalCount(), getPassedCount(), getFailedCount()));
        sb.append(String.format("Execution time: %d ms%n", executionTimeMs));
        sb.append(line);

        return sb.toString();
    }
}