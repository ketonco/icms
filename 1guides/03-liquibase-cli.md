# Liquibase CLI con Gradle

El plugin `org.liquibase.gradle` version `2.2.2` crea tareas como `update`, `status` y `rollback`.

```powershell
.\gradlew.bat :user-auth:tasks --all
.\gradlew.bat :user-auth:update
```

## Runtime

`user-auth/build.gradle` define:

```groovy
liquibaseRuntime 'org.liquibase:liquibase-core'
liquibaseRuntime 'org.postgresql:postgresql'
liquibaseRuntime 'info.picocli:picocli:4.7.6'
liquibaseRuntime sourceSets.main.output
```

La ultima linea permite encontrar los recursos del modulo al ejecutar Liquibase desde el monorepo.

La actividad `main` usa `user-auth/src/main/resources/db/migration-root.yaml`. La ruta es relativa a la raiz al ejecutar `:user-auth:update`.

Se pueden pasar parametros CLI:

```powershell
.\gradlew.bat :user-auth:update `
  -PdbUrl=jdbc:postgresql://localhost:25432/ICMS_UA `
  -PdbUser=postgres `
  -PdbPassword=adm123456
```

Si aparece un error de `exec()`, revisar la compatibilidad del plugin `2.2.2` con Gradle y usar la version 8.x del wrapper.
