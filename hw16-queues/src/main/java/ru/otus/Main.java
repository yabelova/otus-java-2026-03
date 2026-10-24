package ru.otus;

import java.time.LocalDateTime;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriterFake;
import ru.otus.services.processors.SensorDataProcessorBuffered;

public class Main {

    private static final int BUFFER_SIZE = 3;

    public static void main(String[] args) {
        var writer = new SensorDataBufferedWriterFake();
        var processor = new SensorDataProcessorBuffered(BUFFER_SIZE, writer);
        var now = LocalDateTime.now();

        processor.process(new SensorData(now.plusSeconds(3), "room 2", 30d));
        processor.process(new SensorData(now.plusSeconds(1), "room 1", 10d));
        processor.process(new SensorData(now.plusSeconds(2), "room 2", 20d));

        processor.process(new SensorData(now.plusSeconds(4), "room 1", 40d));
        processor.onProcessingEnd();
    }
}
