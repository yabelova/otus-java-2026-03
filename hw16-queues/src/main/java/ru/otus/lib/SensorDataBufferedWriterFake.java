/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L32-concurrentCollections)
 */
package ru.otus.lib;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.model.SensorData;

public class SensorDataBufferedWriterFake implements SensorDataBufferedWriter {
    private static final Logger log = LoggerFactory.getLogger(SensorDataBufferedWriterFake.class);

    @Override
    public void writeBufferedData(List<SensorData> bufferedData) {
        var dataToWrite = bufferedData.stream().map(SensorData::toString).collect(Collectors.joining("\n"));
        log.info("Simulating writing a batch of data somewhere: \n{}", dataToWrite);
    }
}
