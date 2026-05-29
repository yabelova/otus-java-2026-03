package ru.otus;

import ru.otus.model.Loggable;
import ru.otus.proxy.ProxyFactory;

public class Demo {
    public static void main(String[] args) {
        Loggable cafe = ProxyFactory.createLoggable();

        cafe.sayHello();
        cafe.brewCoffee(7);
        cafe.prepareTable(5, 3);
        cafe.addSugar(2, true);
        cafe.orderPastry(101, 2, "Croissant");
        cafe.orderPastry(105, "Bun");
    }
}
