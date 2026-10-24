/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L32-concurrentCollections)
 * Changes: Implemented the buffered processor
 */
package ru.otus.services.processors;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private static final Comparator<SensorData> BY_MEASUREMENT_TIME = Comparator.comparing(SensorData::getMeasurementTime);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final PriorityQueue<SensorData> buffer = new PriorityQueue<>(BY_MEASUREMENT_TIME);
    private final ArrayDeque<List<SensorData>> batchesToWrite = new ArrayDeque<>();

    private final Object buffersLock = new Object();
    private final Object writerLock = new Object();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        if (writer == null) {
            throw new IllegalArgumentException("writer must not be null");
        }
        if (bufferSize < 1) {
            throw new IllegalArgumentException("bufferSize must be greater than 0");
        }
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        boolean shouldFlush = false;
        synchronized (buffersLock) {
            buffer.add(Objects.requireNonNull(data, "data must not be null"));
            if (buffer.size() >= bufferSize) {
                batchesToWrite.addLast(extractBatch(bufferSize));
                shouldFlush = true;
            }
        }
        if (shouldFlush) {
            flushBuffer();
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }

    void flush() {
        synchronized (buffersLock) {
            if (!buffer.isEmpty()) {
                batchesToWrite.addLast(extractBatch(buffer.size()));
            }
        }
        flushBuffer();
    }

    void flushBuffer() {
        synchronized (writerLock) {
            while (true) {
                List<SensorData> dataToWrite;
                synchronized (buffersLock) {
                    dataToWrite = batchesToWrite.pollFirst();
                }
                if (dataToWrite == null) {
                    return;
                }
                try {
                    writer.writeBufferedData(dataToWrite);
                } catch (Exception e) {
                    log.error("Ошибка записи пачки из {} измерений", dataToWrite.size(), e);
                    // Намеренно не повторяем запись и не возвращаем пачку в очередь:
                    // writer мог частично сохранить данные, поэтому повтор может создать дубликаты.
                    // Текущий контракт не требует пробрасывать ошибку или хранить неудачную пачку.
                }
            }
        }
    }

    // Вызывать только при захваченном buffersLock: метод читает и изменяет основной буфер
    private ArrayList<SensorData> extractBatch(int batchSize) {
        var batch = new ArrayList<SensorData>(batchSize);
        while (batch.size() < batchSize && !buffer.isEmpty()) {
            batch.add(buffer.poll());
        }
        return batch;
    }
}
