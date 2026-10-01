# Módulo `user-auth`

## Levantar unicamente el servicio local

```powershell
./gradlew :user-auth:bootRun
```

Levantar unicamente el servicio de user-auth local en el puerto indicado.

## Actualizar la base de datos

```powershell
.\gradlew.bat :user-auth:update
```

Ejecuta la tarea `update` del módulo `user-auth`.

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
