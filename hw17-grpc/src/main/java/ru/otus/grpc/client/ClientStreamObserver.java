package ru.otus.grpc.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.proto.NumberResponse;

import java.util.concurrent.atomic.AtomicLong;

public class ClientStreamObserver
        implements StreamObserver<NumberResponse> {

    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);

    private final AtomicLong lastValue = new AtomicLong();

    @Override
    public void onNext(NumberResponse response) {
        lastValue.set(response.getValue());
        log.info("new value:{}", response.getValue());
    }

    @Override
    public void onError(Throwable error) {
        log.error("gRPC request failed", error);
    }

    @Override
    public void onCompleted() {
        log.info("Stream completed");
    }

    public long takeLastValue() {
        return lastValue.getAndSet(0);
    }
}
