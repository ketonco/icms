# Etapa 1: Construcción
FROM eclipse-temurin:24-jdk-alpine AS builder
WORKDIR /app
# 1. Copia de archivos de configuracion globales (Aprovecha Cache)
COPY gradle gradle
COPY gradlew settings.gradle ./
COPY buildSrc buildSrc    
# 2. Copia de la libreria compartida y el codigo del microservicio
COPY shared-kernel shared-kernel
COPY user-auth user-auth
# 3. Empaquetado del artefacto ejecutable
RUN chmod +x gradlew
RUN ./gradlew :user-auth:bootJar --no-daemon

# Etapa 2: Imagen final de ejecución
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=builder /app/user-auth/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]