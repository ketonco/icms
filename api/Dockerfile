# Etapa 1: Construcción
FROM eclipse-temurin:24-jdk-alpine AS builder
WORKDIR /app

COPY gradle gradle
COPY gradlew settings.gradle ./
COPY api api

RUN ./gradlew :api:bootJar --no-daemon

# Etapa 2: Imagen final de ejecución
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=builder /app/api/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
