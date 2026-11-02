package ru.otus.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.service.NumbersService;

import java.io.IOException;

public class GRPCServer {

    private static final Logger log = LoggerFactory.getLogger(GRPCServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        NumbersService numbersService = new NumbersService();

        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(numbersService)
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down gRPC server...");
            server.shutdown();
        }));

        log.info("gRPC server waiting for client connections on port {}...", SERVER_PORT);
        server.awaitTermination();
    }
}
