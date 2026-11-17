# Docker y Docker Compose

`api/docker-compose.yml` levanta `api-gateway` y `user-auth`. El gateway publica normalmente `8080` y `user-auth` `8081`.

## Requisitos previos

- Tener Docker Engine (o Docker Desktop) instalado y en ejecución, con el plugin de Docker Compose disponible.
- Tener PostgreSQL ejecutándose y accesible desde la red de Docker. Este Compose no crea la base de datos: requiere la red externa `docker_db_network` y que el host indicado por `DB_HOST` sea resoluble desde esa red. En la configuración local documentada, el host es `postgreSQL_DB`, no `localhost`.
- Crear `api/.env` usando `api/.env.example` como plantilla y completar al menos `DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD` y `DB_USER_AUTH_NAME`. Conserva `api/.env` como archivo local; no lo uses para guardar secretos en el repositorio.

El archivo `api/.env` contiene puertos y datos de conexion. Compose sustituye `${VARIABLE}` en el YAML; las variables necesarias tambien se pasan explicitamente en `environment` para que existan dentro de los contenedores.

```powershell
docker compose -f .\api\docker-compose.yml config
docker compose -f .\api\docker-compose.yml up --build -d
docker compose -f .\api\docker-compose.yml ps
docker compose -f .\api\docker-compose.yml logs -f api-gateway
docker compose -f .\api\docker-compose.yml logs -f user-auth
docker compose -f .\api\docker-compose.yml down
```

Dentro de Docker, `user-auth:8081` es un DNS valido. Desde el navegador se usa `http://localhost:8080`; no usar `user-auth` desde el navegador porque ese nombre solo existe en la red interna.

## Variables en Docker

Docker no usa variables de usuario Windows ni la tarea `update` de Gradle. `api/docker-compose.yml` pasa explícito `SPRING_DATASOURCE_URL/USERNAME/PASSWORD` desde `api/.env`:

```powershell
docker compose -f .\api\docker-compose.yml up --build -d
```

Mantén en `api/.env` `SPRING_PROFILES_ACTIVE=dev` (nunca `local`) y `DB_HOST=postgreSQL_DB` (no `localhost`). Si falta `DB_PASSWORD` en `.env`, el contenedor falla en vez de usar la fija. `api/.env` es solo-local; versiona solo `api/.env.example` con valores vacíos.
