package ru.otus.logging;

import java.util.Arrays;

public class LoggingHelper {

    private LoggingHelper() {
    }

    public static void log(String prefix, String methodName, Object... args) {
        StringBuilder sb = new StringBuilder(prefix).append(" executed method: ").append(methodName);
        if (args != null && args.length > 0) {
            String s = Arrays.toString(args);
            sb.append(", params: ").append(s, 1, s.length() - 1);
        }
        System.out.println(sb);
    }
}
