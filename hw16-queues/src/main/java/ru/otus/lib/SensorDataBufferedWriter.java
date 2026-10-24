/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L32-concurrentCollections)
 */
package ru.otus.lib;

import java.util.List;
import ru.otus.api.model.SensorData;

public interface SensorDataBufferedWriter {
    void writeBufferedData(List<SensorData> bufferedData);
}
