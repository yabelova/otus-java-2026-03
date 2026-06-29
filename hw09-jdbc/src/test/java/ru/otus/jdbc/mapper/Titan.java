package ru.otus.jdbc.mapper;

import ru.otus.annotation.Id;

public class Titan {
    @Id
    public int id;
    public String name;
    public int heightMeters;
    public boolean isShifter;
    public String currentHost;

    public Titan() {}

    public Titan( String name, int heightMeters, boolean isShifter, String currentHost) {
        this.name = name;
        this.heightMeters = heightMeters;
        this.isShifter = isShifter;
        this.currentHost = currentHost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeightMeters() {
        return heightMeters;
    }

    public void setHeightMeters(int heightMeters) {
        this.heightMeters = heightMeters;
    }

    public boolean isShifter() {
        return isShifter;
    }

    public void setShifter(boolean shifter) {
        isShifter = shifter;
    }

    public String getCurrentHost() {
        return currentHost;
    }

    public void setCurrentHost(String currentHost) {
        this.currentHost = currentHost;
    }
}

