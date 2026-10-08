# Módulo `api`

## Levantar unicamente el servicio de api-gateway local

```powershell
./gradlew :api:bootRun
```

Levantar unicamente el servicio de api-gateway local en el puerto indicado. No usa base de datos propia.

## Levantar los servicios con Docker Compose

```powershell
docker compose -f .\api\docker-compose.yml up --build -d
docker compose -f .\api\docker-compose.yml up --build -d user-auth
docker compose -f .\api\docker-compose.yml up --build -d api-gateway
```

Construye las imágenes y levanta en segundo plano los servicios definidos en
`api/docker-compose.yml`. Toma las variables de `api/.env` con
`SPRING_PROFILES_ACTIVE=dev` (nunca `local`); no usa el default local ni la
tarea `update` de Gradle.

## Ejecutar todos los tests

```powershell
.\gradlew.bat :api:test
```

Ejecuta todos los `Test` del modulo de `api`. Los tests del gateway usan
downstream controlado (WireMock) y no requieren `user-auth` vivo; detalle en
`1guides/10-gateway-tests.md`. Para pruebas puntuales ver `api-tests.md`.
