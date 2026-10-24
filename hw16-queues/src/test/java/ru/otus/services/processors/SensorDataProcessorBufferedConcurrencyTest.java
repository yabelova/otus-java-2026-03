package ru.otus.services.processors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

class SensorDataProcessorBufferedConcurrencyTest {

    @Test
    void processCanAddDataWhileWriterIsBlocked() throws Exception {
        var writerStarted = new CountDownLatch(1);
        var allowWriterToFinish = new CountDownLatch(1);
        SensorDataBufferedWriter writer = data -> {
            writerStarted.countDown();
            try {
                if (!allowWriterToFinish.await(5, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Timed out waiting to release writer");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
        };
        var processor = new SensorDataProcessorBuffered(2, writer);
        var firstTwo = List.of(
                sensorData(1),
                sensorData(2));

        try (var executor = Executors.newFixedThreadPool(2)) {
            var firstFlush = executor.submit(() -> firstTwo.forEach(processor::process));
            assertThat(writerStarted.await(5, TimeUnit.SECONDS)).isTrue();

            var addWhileWriterBlocked = executor.submit(() -> processor.process(sensorData(3)));
            addWhileWriterBlocked.get(1, TimeUnit.SECONDS);

            allowWriterToFinish.countDown();
            firstFlush.get(5, TimeUnit.SECONDS);
            processor.flush();
        } finally {
            allowWriterToFinish.countDown();
        }
    }

    @Test
    void writesQueuedBatchesInFifoOrderWithoutOverlappingWriterCalls() throws Exception {
        var firstWriteStarted = new CountDownLatch(1);
        var allowFirstWriteToFinish = new CountDownLatch(1);
        var writeCount = new AtomicInteger();
        var activeWrites = new AtomicInteger();
        var maxActiveWrites = new AtomicInteger();
        var writtenBatches = new ArrayList<List<SensorData>>();

        SensorDataBufferedWriter writer = batch -> {
            var active = activeWrites.incrementAndGet();
            maxActiveWrites.accumulateAndGet(active, Math::max);
            try {
                synchronized (writtenBatches) {
                    writtenBatches.add(List.copyOf(batch));
                }
                if (writeCount.incrementAndGet() == 1) {
                    firstWriteStarted.countDown();
                    if (!allowFirstWriteToFinish.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Timed out waiting to release first write");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            } finally {
                activeWrites.decrementAndGet();
            }
        };

        var processor = new SensorDataProcessorBuffered(2, writer);
        var firstBatchThread = new Thread(() -> {
            processor.process(sensorData(1));
            processor.process(sensorData(2));
        });
        var secondBatchThread = new Thread(() -> {
            processor.process(sensorData(3));
            processor.process(sensorData(4));
        });
        var finalFlushThread = new Thread(() -> {
            processor.process(sensorData(5));
            processor.flush();
        });

        try {
            firstBatchThread.start();
            assertThat(firstWriteStarted.await(5, TimeUnit.SECONDS)).isTrue();

            secondBatchThread.start();
            awaitBlocked(secondBatchThread);

            finalFlushThread.start();
            awaitBlocked(finalFlushThread);

            allowFirstWriteToFinish.countDown();
            firstBatchThread.join(5_000);
            secondBatchThread.join(5_000);
            finalFlushThread.join(5_000);

            assertThat(firstBatchThread.isAlive()).isFalse();
            assertThat(secondBatchThread.isAlive()).isFalse();
            assertThat(finalFlushThread.isAlive()).isFalse();
            assertThat(maxActiveWrites.get()).isEqualTo(1);
            assertThat(writtenBatches).containsExactly(
                    List.of(sensorData(1), sensorData(2)),
                    List.of(sensorData(3), sensorData(4)),
                    List.of(sensorData(5)));
        } finally {
            allowFirstWriteToFinish.countDown();
        }
    }

    @Test
    void sensorDataRejectsNullMeasurementTime() {
        assertThrows(NullPointerException.class, () -> new SensorData(null, "Room", 1d));
    }

    private SensorData sensorData(long second) {
        return new SensorData(LocalDateTime.of(2026, 1, 1, 0, 0).plusSeconds(second), "Room", (double) second);
    }

    private void awaitBlocked(Thread thread) throws InterruptedException {
        var deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (thread.isAlive() && thread.getState() != Thread.State.BLOCKED && System.nanoTime() < deadline) {
            Thread.sleep(1);
        }
        assertThat(thread.getState()).isEqualTo(Thread.State.BLOCKED);
    }
}
