package ru.otus.demo;

import ru.otus.model.Loggable;
import ru.otus.model.LoggableImpl;

/**
 * Demonstrates logging via Java Instrumentation agent.
 * Run with {@code -javaagent} to see agent-based logging.
 */
public class AgentDemo {
    public static void main(String[] args) {
        Loggable cafe = new LoggableImpl();

        cafe.sayHello();
        cafe.brewCoffee(7);
        cafe.addSugar(2, true);
        cafe.orderPastry(101, 2, "Croissant");
        cafe.orderPastry(105, "Bun");
    }
}
