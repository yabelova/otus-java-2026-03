package ru.otus.classfileapi;

import java.lang.instrument.Instrumentation;

public class LogAgent {
    public static void premain(String args, Instrumentation inst) {
        System.setProperty("logging.agent.active", "true");
        inst.addTransformer(new LogTransformer());
    }
}
