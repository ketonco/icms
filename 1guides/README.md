# Guias del proyecto ICMS

Estas guias documentan la estructura y configuracion real del monorepo, separadas por tema.

## Orden recomendado

1. [Estructura del proyecto](01-estructura-proyecto.md)
2. [Java y Gradle](02-java-y-gradle.md)
3. [Liquibase CLI](03-liquibase-cli.md)
4. [Crear migraciones](04-crear-migraciones.md)
5. [Docker y Docker Compose](05-docker-compose.md)
6. [API Gateway y comunicacion entre servicios](06-api-gateway.md)
7. [Diagnostico](07-diagnostico.md)

La guia [docker.md](docker.md) conserva los comandos rapidos de Docker Compose existentes.

## Comandos base

Desde la raiz del repositorio:

```powershell
java -version
.\gradlew.bat -version
.\gradlew.bat projects
```

Usar siempre `gradlew.bat` para respetar la version de Gradle del wrapper.
