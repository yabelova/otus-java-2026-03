/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L18-jdbc)
 * Adapted for the hw09-jdbc module structure.
 * Changes: Added @Id annotation on id field.
 */
package ru.otus.model;

import ru.otus.annotation.Id;

public class Client {
    @Id
    private Long id;
    private String name;

    public Client() {}

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
