# Docker y Docker Compose

`api/docker-compose.yml` levanta `api-gateway` y `user-auth`. El gateway publica normalmente `8080` y `user-auth` `8081`.

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
