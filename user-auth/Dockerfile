# Etapa 1: Construcción
FROM eclipse-temurin:24-jdk-alpine AS builder
WORKDIR /app
# 1. Copia de archivos de configuracion globales (Aprovecha Cache)
COPY gradle gradle
COPY gradlew settings.gradle ./

# 2. Copia de la libreria compartida y el codigo del microservicio
COPY gradle gradle
COPY gradlew gradlew.bat settings.gradle ./
COPY shared-kernel shared-kernel
COPY user-auth user-auth

RUN ./gradlew :user-auth:bootJar --no-daemon

# Etapa 2: Imagen final de ejecución
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=builder /app/user-auth/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]