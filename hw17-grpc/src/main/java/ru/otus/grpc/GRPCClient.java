package ru.otus.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.client.ClientStreamObserver;
import ru.otus.grpc.proto.NumbersRequest;
import ru.otus.grpc.proto.NumbersServiceGrpc;

import java.util.concurrent.TimeUnit;

public class GRPCClient {

    private static final Logger log = LoggerFactory.getLogger(GRPCClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        try {
            ClientStreamObserver observer = new ClientStreamObserver();
            NumbersRequest request = NumbersRequest.newBuilder()
                    .setFirstValue(0)
                    .setLastValue(30)
                    .build();

            var stub = NumbersServiceGrpc.newStub(channel);
            stub.generateNumbers(request, observer);

            long currentValue = 0;
            for (int i = 0; i <= 50; i++) {
                currentValue += observer.takeLastValue() + 1;
                log.info("i:{} ,currentValue:{}", i, currentValue);
                Thread.sleep(1_000);
            }
        } finally {
            channel.shutdown();

            if (!channel.awaitTermination(15, TimeUnit.SECONDS)) {
                channel.shutdownNow();
            }
        }
    }
}
