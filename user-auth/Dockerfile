# Etapa 1: Construcción
FROM eclipse-temurin:24-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# Etapa 2: Imagen final de ejecución
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]