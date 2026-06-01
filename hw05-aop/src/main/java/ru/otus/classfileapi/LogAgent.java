package ru.otus.classfileapi;

import java.lang.instrument.Instrumentation;

public class LogAgent {
    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer(new LogTransformer());
    }
}
