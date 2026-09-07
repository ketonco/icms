# Diagnostico rapido

## Gradle pide Java 21

Revisar `java -version`, `JAVA_HOME` y cualquier `JavaLanguageVersion.of(21)` en los `build.gradle`. El proyecto usa Java 24. Ejecutar `.\gradlew.bat --stop` y repetir la tarea.

## Liquibase falla con `exec()`

Puede ser una incompatibilidad entre `org.liquibase.gradle` `2.2.2` y una version de Gradle no soportada. Usar la version 8.x definida por el wrapper, no una instalacion global distinta.

## Falta `migration-root.yaml`

```powershell
Test-Path .\user-auth\src\main\resources\db\migration-root.yaml
```

Al ejecutar `:user-auth:update` desde la raiz, el `changelogFile` debe ser `user-auth/src/main/resources/db/migration-root.yaml`. No editar copias dentro de `build/` o `bin/`.

## 404 ResourceWebHandler

Revisar que se use `spring.cloud.gateway.server.webflux`, que `routes` este dentro de `webflux`, que el `Path` coincida y que la imagen del gateway se haya reconstruido.

## Redireccion a `user-auth:8081/login`

La ruta funciona, pero Spring Security redirige una peticion HTML. Comparar respuestas:

```powershell
curl.exe -i -H "Accept: text/html" http://localhost:8080/api/v1/auth/test
curl.exe -i -H "Accept: application/json" http://localhost:8080/api/v1/auth/test
```

Si aparece `Location: http://user-auth:8081/login`, permitir el endpoint de prueba o devolver `401` sin redireccion. No cambiar el `uri` del gateway a `localhost`, porque romperia la red interna Docker.
