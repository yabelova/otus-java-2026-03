# ===== Stage 1: сборка fat JAR =====
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /project
# Копируем весь проект (нужен settings.gradle.kts для многомодульности)
COPY . .
# Собираем только наш модуль
RUN ./gradlew :l09-docker:shadowJar

# ===== Stage 2: минимальный образ для запуска =====
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
# Копируем только JAR из первого стейджа
COPY --from=builder /project/l09-docker/build/libs/l09-docker-all.jar app.jar
# Подсказка Docker: эта директория хранит данные приложения
VOLUME /app/logs
CMD ["java", "-jar", "app.jar"]
