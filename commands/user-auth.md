# Módulo `user-auth`

## Variables de usuario (1 vez por PC, sin secretos en repo)

```powershell
[Environment]::SetEnvironmentVariable("DB_HOST","localhost","User")
[Environment]::SetEnvironmentVariable("DB_PORT","25432","User")
[Environment]::SetEnvironmentVariable("DB_USER","postgres","User")
[Environment]::SetEnvironmentVariable("DB_USER_AUTH_NAME","ICMS_UA","User")
[Environment]::SetEnvironmentVariable("DB_PASSWORD","xxx","User")
```

Reinicia la terminal. Si falta `DB_PASSWORD`, `bootRun` y `update` fallan.

## Levantar el servicio (usa variables de usuario)

```powershell
./gradlew :user-auth:bootRun
./gradlew :user-auth:bootRun --args='--spring.profiles.active=prod'
```

## Actualizar la base de datos (usa variables de usuario)

```powershell
.\gradlew.bat :user-auth:update
.\gradlew.bat :user-auth:update -PdbUrl=jdbc:postgresql://host:puerto/DB -PdbUser=postgres -PdbPassword=xxx
```

## Eliminar todos los datos

```powershell
.\gradlew.bat :user-auth:dropAll
```

Ejecuta la tarea `dropAll` del módulo `user-auth`. Usar con precaución, ya que
puede eliminar todos los datos administrados por el módulo.

## Ejecutar todos los tests

```powershell
.\gradlew.bat :user-auth:test
```

Ejecuta todos los `Test` del modulo de `user-auth`. Para pruebas puntuales ver
`user-auth-tests.md`.

## Compilar el módulo

```powershell
.\gradlew.bat :user-auth:build
```

Compila `user-auth`, ejecuta sus pruebas y genera el artefacto de la
aplicación.

## Ejecutar los Seeds (CLI)

```powershell
.\gradlew :user-auth:bootRun --args='--spring.profiles.active=task --seed=<Nombre|all>'
```

Valores de `<Nombre>`: `LanguageDataSeed`, `UserStatusDataSeed`,
`UserStatusTranslationDataSeed`, `UserTypeDataSeed`,
`UserTypeTranslationDataSeed`, `PermissionDataSeed` (o `all` para todos en
orden de prioridad).

Ejecuta los Seeds de `user-auth` para llenar la base de datos con informacion base
