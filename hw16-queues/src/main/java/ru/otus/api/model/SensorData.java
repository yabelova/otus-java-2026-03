/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L32-concurrentCollections)
 * Changes: Added equals/hashCode.
 */
package ru.otus.api.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class SensorData {
    private final LocalDateTime measurementTime;
    private final String room;
    private final Double value;

    public SensorData(LocalDateTime measurementTime, String room, Double value) {
        this.measurementTime = Objects.requireNonNull(measurementTime, "measurementTime must not be null");
        this.room = room;
        this.value = value;
    }

    public LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public String getRoom() {
        return room;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SensorData other)) {
            return false;
        }
        return Objects.equals(measurementTime, other.measurementTime)
                && Objects.equals(room, other.room)
                && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurementTime, room, value);
    }

    @Override
    public String toString() {
        return "SensorData{" + "measurementTime="
                + measurementTime + ", room='"
                + room + '\'' + ", value="
                + value + '}';
    }
}
