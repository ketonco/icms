# Etapa 1: Construcción
FROM eclipse-temurin:24-jdk-alpine AS builder
WORKDIR /app
# 1. Copia de infraestructura global de Gradle (Aprovecha la caché de Docker)
COPY gradle gradle
COPY gradlew settings.gradle ./ 
COPY buildSrc buildSrc
# 2. Copia de módulos requeridos para este servicio
COPY shared-kernel shared-kernel
COPY api api
# 3. Empaquetado del artefacto executable
RUN chmod +x gradlew
RUN ./gradlew :api:bootJar --no-daemon

# Etapa 2: Imagen final de ejecución
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=builder /app/api/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
