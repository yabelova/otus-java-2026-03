package ru.otus.grpc.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.otus.grpc.proto.NumberResponse;

class ClientStreamObserverTest {

    private final ClientStreamObserver observer = new ClientStreamObserver();

    @Test
    void shouldReturnZeroBeforeReceivingMessages() {
        assertThat(observer.takeLastValue()).isZero();
    }

    @Test
    void shouldReturnReceivedValueOnlyOnce() {
        observer.onNext(response(5));

        assertThat(observer.takeLastValue()).isEqualTo(5);
        assertThat(observer.takeLastValue()).isZero();
    }

    @Test
    void shouldReturnOnlyLatestReceivedValue() {
        observer.onNext(response(3));
        observer.onNext(response(4));
        observer.onNext(response(5));

        assertThat(observer.takeLastValue()).isEqualTo(5);
        assertThat(observer.takeLastValue()).isZero();
    }

    @Test
    void shouldAcceptNewValueAfterPreviousValueWasTaken() {
        observer.onNext(response(5));
        assertThat(observer.takeLastValue()).isEqualTo(5);

        observer.onNext(response(6));

        assertThat(observer.takeLastValue()).isEqualTo(6);
        assertThat(observer.takeLastValue()).isZero();
    }

    private NumberResponse response(long value) {
        return NumberResponse.newBuilder().setValue(value).build();
    }
}
