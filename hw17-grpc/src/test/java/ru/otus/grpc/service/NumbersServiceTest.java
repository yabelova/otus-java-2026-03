package ru.otus.grpc.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import ru.otus.grpc.proto.NumberResponse;
import ru.otus.grpc.proto.NumbersRequest;

class NumbersServiceTest {

    private final NumbersService service = new NumbersService(0);

    @Test
    void shouldStreamNumbersAfterFirstValueThroughLastValueAndComplete() {
        RecordingObserver observer = new RecordingObserver();
        NumbersRequest request = NumbersRequest.newBuilder()
                .setFirstValue(5)
                .setLastValue(8)
                .build();

        service.generateNumbers(request, observer);

        assertThat(observer.events).containsExactly(6L, 7L, 8L, "completed");
    }

    @Test
    void shouldRejectRequestWhenBoundsAreEqual() {
        RecordingObserver observer = new RecordingObserver();
        NumbersRequest request = NumbersRequest.newBuilder()
                .setFirstValue(5)
                .setLastValue(5)
                .build();

        service.generateNumbers(request, observer);

        assertInvalidArgument(observer);
    }

    @Test
    void shouldRejectRequestWhenFirstValueExceedsLastValue() {
        RecordingObserver observer = new RecordingObserver();
        NumbersRequest request = NumbersRequest.newBuilder()
                .setFirstValue(7)
                .setLastValue(3)
                .build();

        service.generateNumbers(request, observer);

        assertInvalidArgument(observer);
    }

    private void assertInvalidArgument(RecordingObserver observer) {
        assertThat(observer.events).hasSize(1);
        assertThat(observer.events.getFirst()).isInstanceOfSatisfying(StatusRuntimeException.class, error -> {
            assertThat(error.getStatus().getCode()).isEqualTo(Status.Code.INVALID_ARGUMENT);
            assertThat(error.getStatus().getDescription())
                    .isEqualTo("firstValue must be less than lastValue");
        });
    }

    private static class RecordingObserver implements StreamObserver<NumberResponse> {
        private final List<Object> events = new ArrayList<>();

        @Override
        public void onNext(NumberResponse response) {
            events.add(response.getValue());
        }

        @Override
        public void onError(Throwable error) {
            events.add(error);
        }

        @Override
        public void onCompleted() {
            events.add("completed");
        }
    }
}
