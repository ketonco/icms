# Estructura del proyecto

ICMS es un monorepo Gradle multi-project:

```text
ICMS/
  settings.gradle
  gradlew.bat
  gradle/wrapper/
  api/
  user-auth/
  1guides/
```

## Modulo api

`api` es el API Gateway reactivo. Usa Spring Cloud Gateway con WebFlux y expone el puerto publico del gateway. Sus archivos principales son `api/build.gradle`, `api/src/main/resources/application.yml`, `api/Dockerfile` y `api/docker-compose.yml`.

## Modulo user-auth

`user-auth` es el microservicio de autenticacion. Usa Spring MVC, Spring Data JPA, PostgreSQL, Spring Security y Liquibase. Sus archivos principales son `user-auth/build.gradle`, `user-auth/src/main/resources/application.yml`, `user-auth/src/main/resources/db/migration-root.yaml`, `user-auth/src/main/resources/db/migrations/` y `user-auth/Dockerfile`.

`settings.gradle` declara los modulos `api` y `user-auth`. Los directorios `build/` y `bin/` son generados y no deben editarse manualmente.
