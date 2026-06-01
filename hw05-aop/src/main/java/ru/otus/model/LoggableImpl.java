package ru.otus.model;

import ru.otus.annotation.Log;

public class LoggableImpl implements Loggable {

    @Override
    @Log
    public void sayHello() {
        System.out.println("Hello\n");
    }

    @Override
    @Log
    public void brewCoffee(int strength) {
        System.out.println("Brewing coffee strength " + strength + "/10\n");
    }

    @Override
    public void addSugar(int spoons, boolean isBrown) {
        String type = isBrown ? "brown" : "white";
        System.out.println("Added " + spoons + " spoons of " + type + " sugar\n");
    }

    @Override
    @Log
    public void orderPastry(int itemCode, int qty, String name) {
        System.out.println("Order: " + name + " x" + qty + " (code " + itemCode + ")\n");
    }

    @Override
    public void orderPastry(int itemCode, String name) {
        System.out.println("Order: " + name + " (code " + itemCode + ")\n");
    }
}
