# Ejecutar un servicio específico de Docker Compose

Este comando permite construir y ejecutar únicamente el servicio `api-gateway` utilizando un archivo `docker-compose.yml` ubicado en la carpeta `api`.

## Comando

```powershell
docker compose -f .\api\docker-compose.yml up --build -d api-gateway
```

## Descripción de los parámetros

- `docker compose`: ejecuta Docker Compose.
- `-f .\api\docker-compose.yml`: especifica el archivo de configuración que se utilizará. La ruta es relativa al directorio actual.
- `up`: crea e inicia los contenedores definidos en el archivo de Compose.
- `--build`: reconstruye las imágenes antes de iniciar los contenedores. Es útil cuando se modificó el código o el `Dockerfile`.
- `-d`: ejecuta los contenedores en segundo plano (*detached mode*), por lo que la terminal queda disponible.
- `api-gateway`: indica el servicio específico que se desea iniciar.

## Requisitos

1. Ejecutar el comando desde el directorio que contiene la carpeta `api`.
2. Tener Docker instalado y en ejecución.
3. Verificar que el servicio `api-gateway` esté definido en `api\docker-compose.yml`.

## Verificar el estado del servicio

Para consultar los contenedores activos después de ejecutar el comando:

```powershell
docker compose -f .\api\docker-compose.yml ps
```

Para consultar los registros del servicio:

```powershell
docker compose -f .\api\docker-compose.yml logs -f api-gateway
```

## Detener el servicio

Para detener y eliminar los contenedores creados por ese archivo de Compose:

```powershell
docker compose -f .\api\docker-compose.yml down
```

