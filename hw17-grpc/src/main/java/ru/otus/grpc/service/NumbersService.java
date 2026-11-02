package ru.otus.grpc.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ru.otus.grpc.proto.NumberResponse;
import ru.otus.grpc.proto.NumbersRequest;
import ru.otus.grpc.proto.NumbersServiceGrpc;

public class NumbersService
        extends NumbersServiceGrpc.NumbersServiceImplBase {

    private final long delayMillis;

    public NumbersService() {
        this(2_000);
    }

    NumbersService(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    @Override
    public void generateNumbers(NumbersRequest request, StreamObserver<NumberResponse> responseObserver) {

        long firstValue = request.getFirstValue();
        long lastValue = request.getLastValue();

        if (firstValue >= lastValue) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("firstValue must be less than lastValue")
                    .asRuntimeException());
            return;
        }

        for (long value = firstValue; value < lastValue; ) {
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                responseObserver.onError(
                        Status.CANCELLED
                                .withDescription("Number generation interrupted")
                                .withCause(exception)
                                .asRuntimeException());
                return;
            }

            value++;

            NumberResponse response = NumberResponse.newBuilder()
                    .setValue(value)
                    .build();

            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }
}
