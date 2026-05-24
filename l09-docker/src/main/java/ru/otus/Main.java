package ru.otus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        String message = "Log was record at: " + LocalDateTime.now() + " | Java: " + System.getProperty("java.version") + "\n";

        // Выводим в консоль, как раньше
        System.out.print("Container is working... " + message);

        try {
            // Путь к папке внутри ЛИНУКС-контейнера
            Path logDir = Path.of("/app/logs");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            // Записываем строчку в файл внутри контейнера
            Path logFile = logDir.resolve("app.log");
            Files.writeString(logFile, message, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("File was successfully saved to /app/logs/app.log");

        } catch (IOException e) {
            System.err.println("File io exception: " + e.getMessage());
        }
    }
}
