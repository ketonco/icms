# Comandos del proyecto ICMS

> Agrega los nuevos comandos en el módulo o caso correspondiente. Mantén una
> breve explicación debajo de cada comando.

## Gradle — proyecto principal

### Listar módulos del monorepo

```powershell
./gradlew projects
```

Muestra la lista de proyectos o módulos anidados configurados en el proyecto
principal de Gradle.

## Módulo `user-auth`

### Actualizar la base de datos

```powershell
.\gradlew.bat :user-auth:update
```

Ejecuta la tarea `update` del módulo `user-auth`.

### Eliminar todos los datos

```powershell
.\gradlew.bat :user-auth:dropAll
```

Ejecuta la tarea `dropAll` del módulo `user-auth`. Usar con precaución, ya que
puede eliminar todos los datos administrados por el módulo.

### Comando para unit test

```powershell
.\gradlew.bat :user-auth:test
```

Ejecuta todos los `Test` del modulo de `user-auth`

Lista de todos los comandos para test unitario

```powershell
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.controller.TestControllerIntegrationTest"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.controller.TestControllerIntegrationTest.testAuthHealthDirect"
```

## Módulo `api`

### Levantar los servicios con Docker Compose

```powershell
docker compose -f .\api\docker-compose.yml up --build -d
```

Construye las imágenes y levanta en segundo plano los servicios definidos en
`api/docker-compose.yml`.

### Comando para unit test

```powershell
.\gradlew.bat :api:test
```

Ejecuta todos los `Test` del modulo de `api`

Lista de todos los comandos para test unitario

```powershell
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIntegrationTest"
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIntegrationTest.testGatewayRoutingToUserAuth"
```

## Plantilla para nuevos comandos

### Nombre del caso o acción

```powershell
# Escribe aquí el comando
```

Explica brevemente qué hace, qué módulo afecta y cualquier precaución o
requisito necesario para ejecutarlo.