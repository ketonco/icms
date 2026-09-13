# Comandos del proyecto ICMS

> Agrega los nuevos comandos en el módulo o caso correspondiente. Mantén una
> breve explicación debajo de cada comando.

## Gradle — proyecto principal

### Detener procesos de Gradle
 
```powershell
.\gradlew.bat --stop
```

Detiene los Gradle Daemons en ejecución. Útil después de cambios en la
configuración de Gradle o para liberar recursos.

### Limpiar artefactos generados

```powershell
.\gradlew.bat clean
```

Elimina los directorios `build` generados por todos los módulos del monorepo.

### Listar módulos del monorepo

```powershell
./gradlew projects
```

Muestra la lista de proyectos o módulos anidados configurados en el proyecto
principal de Gradle.

### Refrescar Dependencias

```powershell
./gradlew --refresh-dependencies build -x test
```
Refresca las dependencias de cada modulo en un solo comando, bastante util al agregar una dependencia nueva ejecutarlo

## Módulo `user-auth`

### Levantar unicamente el servicio de user-auth local

```powershell
./gradlew :user-auth:bootRun
```

Levantar unicamente el servicio de user-auth local en el puerto indicado.

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

.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.mappers.LanguageMapperTest"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.mappers.LanguageMapperTest.mapFromEntityToDto"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.mappers.LanguageMapperTest.mapFromDtoToEntity"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.service.LanguageServiceTest"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.service.LanguageServiceTest.testGetLanguageByCode"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.repository.LanguageRepositoryTest"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.repository.LanguageRepositoryTest.testSaveAndRetrieveLanguage"

.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.mappers.UserStatusMapperTest"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.mappers.UserStatusMapperTest.mapFromEntityToDto"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.mappers.UserStatusMapperTest.mapFromDtoToEntity"

.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.exceptions.UserAuthGlobalExceptions"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.exceptions.UserAuthGlobalExceptions.testGlobalExceptionHandlingForNonExistentEntity"
```

### Compilar el módulo

```powershell
.\gradlew.bat :user-auth:build
```

Compila `user-auth`, ejecuta sus pruebas y genera el artefacto de la
aplicación.

### Ejecutar los Seeds (CLI)

```powershell
.\gradlew :user-auth:bootRun --args='--spring.profiles.active=task --seed=all'
.\gradlew :user-auth:bootRun --args='--spring.profiles.active=task --seed=LanguageDataSeed'
.\gradlew :user-auth:bootRun --args='--spring.profiles.active=task --seed=UserStatusDataSeed'
```

Ejecuta los Seeds de `user-auth` para llenar la base de datos con informacion base

## Módulo `shared-kernel`

### Listar tareas disponibles

```powershell
.\gradlew.bat :shared-kernel:tasks
```

Muestra las tareas disponibles para la librería `shared-kernel`.

### Compilar la librería

```powershell
.\gradlew.bat :shared-kernel:build
```

Compila `shared-kernel`, ejecuta sus pruebas y genera el JAR de la librería.

## Módulo `api`

### Levantar unicamente el servicio de api-gateway local

```powershell
./gradlew :api:bootRun
```

Levantar unicamente el servicio de api-gateway local en el puerto indicado.

### Levantar los servicios con Docker Compose

```powershell
docker compose -f .\api\docker-compose.yml up --build -d
docker compose -f .\api\docker-compose.yml up --build -d user-auth
docker compose -f .\api\docker-compose.yml up --build -d api-gateway
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
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIntegrationTest.testGatewayRoutingToUserAuthLanguages"
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIntegrationTest.testGatewayRoutingToUserAuthLanguageByCode"
```

## Plantilla para nuevos comandos

### Nombre del caso o acción

```powershell
# Escribe aquí el comando
```

Explica brevemente qué hace, qué módulo afecta y cualquier precaución o
requisito necesario para ejecutarlo.

./gradlew :user-auth:bootRun