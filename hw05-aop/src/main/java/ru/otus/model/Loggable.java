package ru.otus.model;

public interface Loggable {
    void sayHello();

    void prepareTable(int tableNum, int guests);

    void brewCoffee(int strength);

    void addSugar(int spoons, boolean isBrown);

    void orderPastry(int itemCode, String name);

    void orderPastry(int itemCode, int qty, String name);
}
