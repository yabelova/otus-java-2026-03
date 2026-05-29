package ru.otus.model;

import ru.otus.annotation.LogByAgent;
import ru.otus.annotation.LogByProxy;

public class LoggableImpl implements Loggable {

    @Override
    // logged by both mechanisms when agent is active
    @LogByProxy
    @LogByAgent
    public void sayHello() {
        System.out.println("Hello\n");
    }

    @Override
    @LogByAgent
    public void prepareTable(int tableNum, int guests) {
        System.out.println("Table " + tableNum + " set for " + guests + "\n");
    }

    @Override
    @LogByProxy
    public void brewCoffee(int strength) {
        System.out.println("Brewing coffee strength " + strength + "/10\n");
    }

    @Override
    public void addSugar(int spoons, boolean isBrown) {
        String type = isBrown ? "brown" : "white";
        System.out.println("Added " + spoons + " spoons of " + type + " sugar\n");
    }

    @Override
    // logged by both mechanisms when agent is active
    @LogByProxy
    @LogByAgent
    public void orderPastry(int itemCode, int qty, String name) {
        System.out.println("Order: " + name + " x" + qty + " (code " + itemCode + ")\n");
    }

    @Override
    public void orderPastry(int itemCode, String name) {
        System.out.println("Order: " + name + " (code " + itemCode + ")\n");
    }
}
