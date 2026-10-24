/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L32-concurrentCollections)
 */
package ru.otus.api;

import ru.otus.api.model.SensorData;

public interface SensorDataProcessor {
    void process(SensorData data);

    default void onProcessingEnd() {}
}
