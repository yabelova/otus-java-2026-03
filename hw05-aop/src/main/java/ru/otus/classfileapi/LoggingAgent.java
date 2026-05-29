package ru.otus.classfileapi;

import java.lang.instrument.Instrumentation;

public class LoggingAgent {
    public static void premain(String args, Instrumentation inst) {
        System.setProperty("logging.agent.active", "true");
        inst.addTransformer(new LoggingTransformer());
    }
}
