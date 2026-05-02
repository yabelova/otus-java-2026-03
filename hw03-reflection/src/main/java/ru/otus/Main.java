package ru.otus;

import ru.otus.framework.engine.TestEngine;
import ru.otus.tests.BankAccountTest;

public class Main {
    public static void main() {
        TestEngine.run(BankAccountTest.class.getName());
    }
}