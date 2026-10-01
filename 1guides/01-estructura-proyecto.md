# Estructura del proyecto

ICMS es un monorepo Gradle multi-project:

```text
ICMS/
  settings.gradle
  gradlew / gradlew.bat
  gradle/
    libs.versions.toml
    wrapper/
  buildSrc/
  api/
  user-auth/
  shared-kernel/
  commands/
    gradle.md
    user-auth.md
    user-auth-tests.md
    shared-kernel.md
    api.md
  commands.md
  .opencode/
    commands/
  .github/
  1guides/
  AGENTS.md
  MEMORY.md
  pending.md
```

`settings.gradle` declara los modulos `api`, `user-auth` y `shared-kernel`.
Los directorios `build/` y `bin/` son generados y no deben editarse manualmente.

## Modulo api

`api` es el API Gateway reactivo. Usa Spring Cloud Gateway con WebFlux y expone el puerto publico del gateway. Sus archivos principales son `api/build.gradle`, `api/src/main/resources/application.yml`, `api/Dockerfile` y `api/docker-compose.yml`.

## Modulo user-auth

`user-auth` es el microservicio de autenticacion. Usa Spring MVC, Spring Data JPA, PostgreSQL, Spring Security y Liquibase. Sus archivos principales son `user-auth/build.gradle`, `user-auth/src/main/resources/application.yml`, `user-auth/src/main/resources/db/migration-root.yaml`, `user-auth/src/main/resources/db/migrations/` y `user-auth/Dockerfile`. Depende de `shared-kernel` (`project(':shared-kernel')` en su `build.gradle`).

## Modulo shared-kernel

`shared-kernel` es la libreria interna con el codigo compartido: entidades base auditables (`entity/`), repositorios genericos (`repository/`), servicios y reglas de negocio base (`service/`, `rules/`), controladores genericos (`controller/`), DTOs y mappers (`dto/`, `config/mapper/`), manejo global de excepciones (`config/exception/`) e i18n (`resources/i18n/`). No es un servicio desplegable; los microservicios la consumen como dependencia.

## Configuracion y gestion del proyecto

- `gradle/libs.versions.toml`: catálogo centralizado de versiones; ningun `build.gradle` declara versiones explicitas.
- `buildSrc/`: plugins de convencion en Kotlin DSL (`spring-web-conventions`, `spring-jpa-conventions`, `migration-conventions`, etc.).
- `commands.md` + `commands/`: guia de comandos por ambito (Gradle general y por modulo); `commands.md` es el indice.
- `.opencode/commands/`: comandos de agente (`/new-migration`, `/update-guides`).
- `.github/copilot-instructions.md`: convenciones de codigo y arquitectura que todo agente debe leer antes de trabajar.
- `AGENTS.md`, `MEMORY.md`, `pending.md`: reglas de trabajo, memoria esencial del proyecto y pendientes activos.
