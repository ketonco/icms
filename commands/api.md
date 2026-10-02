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

Ejecuta todos los `Test` del modulo de `api`.

## Pruebas puntuales (patrón `--tests`, acepta comodines `*`)

```powershell
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIntegrationTest"
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIntegrationTest.testGatewayRoutingToUserAuth"
```

Para un método puntual usa `"Clase.metodo"`; para un paquete completo, `"paquete.*"`.
