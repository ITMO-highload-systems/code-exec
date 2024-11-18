# Используем базовый образ с JDK 21
FROM openjdk:21-jdk-slim

WORKDIR /app

# Копируем сгенерированный jar файл
COPY build/libs/notion-code-exec-0.0.1-SNAPSHOT.jar app.jar

# Устанавливаем права на файл, если нужно
RUN chmod +x /app/app.jar && apt-get update && apt-get install -y docker.io

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
