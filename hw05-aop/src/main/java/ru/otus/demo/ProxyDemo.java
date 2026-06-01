package ru.otus.demo;

import ru.otus.model.Loggable;
import ru.otus.model.LoggableImpl;
import ru.otus.proxy.ProxyFactory;

/**
 * Demonstrates logging via JDK dynamic proxy.
 * Run without {@code -javaagent} to see only proxy-based logging.
 */
public class ProxyDemo {
    public static void main(String[] args) {
        Loggable cafe = ProxyFactory.create(new LoggableImpl());

        cafe.sayHello();
        cafe.brewCoffee(7);
        cafe.addSugar(2, true);
        cafe.orderPastry(101, 2, "Croissant");
        cafe.orderPastry(105, "Bun");
    }
}
