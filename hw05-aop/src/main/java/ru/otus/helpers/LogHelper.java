package ru.otus.helpers;

import java.util.Arrays;

public class LogHelper {

    private LogHelper() {
    }

    /**
     * Prints {@code "{prefix} executed method: {name}, params: [...]"} to stdout.
     */
    public static void log(String prefix, String methodName, Object... args) {
        StringBuilder sb = new StringBuilder(prefix).append(" executed method: ").append(methodName);
        if (args != null && args.length > 0) {
            sb.append(", params: ").append(Arrays.toString(args));
        }
        System.out.println(sb);
    }
}
