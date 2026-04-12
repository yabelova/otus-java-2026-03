package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        useGradleModuleImplementation();

        useLogging();
    }

    private static void useGradleModuleImplementation() {
        GreetingService gs = new GreetingService();
        System.out.println(gs.greet("Arthur Dent"));
    }

    private static void useLogging() {
        MDC.put("user_id", "42");
        MDC.put("thread_id", String.valueOf(Thread.currentThread().threadId()));

        logger.trace("This TRACE log will not appear anywhere");
        logger.debug("This DEBUG log will be in files only");
        logger.info("This INFO log will appear in both console and file");

        try {
            logger.warn("This WARN will be in console and file - dangerous operation");
            doException();
        } catch (Exception ex) {
            logger.error("This ERROR will be everywhere: {}", ex.getMessage(), ex);
        }

        MDC.clear();
    }

    private static void doException() throws Exception {
        throw new Exception("Test exception for verification");
    }
}
