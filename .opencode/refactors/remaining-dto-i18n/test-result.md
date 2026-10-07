# Test Result: remaining-dto-i18n

## Veredicto

PASS

Todos los tests del plan están en verde (52/52 nuevos, 28/28 de regresión
i18n previa, compilación y `:shared-kernel:test` en BUILD SUCCESSFUL).
El único rojo del suite completo (`UserControllerTest.testCreateUser`) es un
fallo preexistente y ajeno a esta refactorización, verificado en un worktree
prístino de HEAD sin cambios del plan; no bloquea el veredicto.

## Tests ejecutados

### Compilación

- Comando: `.\gradlew.bat :user-auth:compileJava :shared-kernel:compileJava`
- Resultado: BUILD SUCCESSFUL (11 tareas, `UP-TO-DATE`).

### IT nuevos de la refactor (52/52 en verde)

- Comando: `.\gradlew.bat :user-auth:test --tests
  "*CreateUserDtoValidationIT*" --tests "*UserProfileDtoValidationIT*"
  --tests "*LanguageDtoValidationIT*" --tests "*PermissionDtoValidationIT*"`
- Resultado: BUILD SUCCESSFUL.
- Conteo leído de los XML en `user-auth/build/test-results/test/`:
  - `CreateUserDtoValidationIT`: 18 tests, 0 fallos, 0 errores.
  - `UserProfileDtoValidationIT`: 12 tests, 0 fallos, 0 errores.
  - `LanguageDtoValidationIT`: 14 tests, 0 fallos, 0 errores.
  - `PermissionDtoValidationIT`: 8 tests, 0 fallos, 0 errores.

### Regresión i18n previa (28/28 en verde)

- Comando: `.\gradlew.bat :user-auth:test --tests
  "*UserTypeDtoValidationIT*" --tests "*UserStatusDtoValidationIT*" --tests
  "*UserStatusTranslationDtoValidationIT*" --tests
  "*UserTypeTranslationDtoValidationIT*"`
- Resultado: BUILD SUCCESSFUL.
- Conteo: 8 + 8 + 6 + 6 = 28 tests, 0 fallos, 0 errores.

### Tests de shared-kernel (bundles)

- Comando: `.\gradlew.bat :shared-kernel:test`
- Resultado: BUILD SUCCESSFUL (`:shared-kernel:test NO-SOURCE`, el módulo no
  tiene tests propios; los bundles se validan vía los IT de `user-auth`).

### Suite completo de user-auth

- Comando: `.\gradlew.bat :user-auth:test`
- Resultado: 120 tests, 1 fallo (ver sección siguiente), BUILD FAILED solo
  por ese fallo preexistente.

## Fallo preexistente ajeno a la refactor

- Test: `UserControllerTest.testCreateUser`
  (`user-auth/src/test/java/com/icms/user_auth/controller/UserControllerTest.java:72`).
- Mensaje: `Expected status code <200> but was <400>`.
- Por qué es ajeno a esta refactor:
  - El `git diff` de los cuatro DTOs
    (`CreateUserDto.java`, `UserProfileDto.java`, `LanguageDto.java`,
    `PermissionDto.java`) muestra que solo cambió el atributo `message` de
    las anotaciones; regex, límites (`min`/`max`) y anotaciones están
    intactos, por lo que el resultado pasa/falla de la validación es
    idéntico antes y después.
  - El payload del test (`username` `testuser2`, password
    `Testpassword.123456`, emails válidos, `firstName`/`lastName` de 4+
    caracteres) satisface todas las restricciones en ambas versiones, así
    que el 400 no puede venir del cambio de mensajes.
  - Reproducción en código prístino: worktree temporal de HEAD (`bef94f0`,
    sin ningún cambio del plan) contra la misma BD compartida, comando
    `.\gradlew.bat :user-auth:test --tests "*UserControllerTest*"` →
    mismo fallo (1 test, 1 fallo, `Expected status code <200> but was
    <400>`). El worktree se eliminó tras la comprobación.
  - Hipótesis coherente con lo reportado en `executed.md`: fila residual
    `testuser2` en la BD compartida (el `tearDown` es `@Transactional` en
    `UserControllerTest.java:35-41` y sus borrados hacen rollback), por lo
    que la creación repetida devuelve 400 por duplicado.
- Acción: no se tocó el test ni el código (regla: solo ejecutar y reportar);
  se deja al orquestador su re-planificación (posible punto en
  `pending.md`).

## Verificación de archivos del plan

- `CreateUserDto.java:13-27`, `UserProfileDto.java:13-23`,
  `LanguageDto.java:10-19`, `PermissionDto.java:10-15`: solo cambian los
  textos de `message` por claves `{...}`; verificado con `git diff`.
- `messages.properties:74-105` y `messages_es.properties:74-105`: 26 claves
  nuevas en 4 secciones, sin colisiones con las secciones existentes.
- Los 4 IT nuevos existen en las rutas del plan y aportan 52 tests en verde.

## Opcional

- Corregir `UserControllerTest` (limpieza real de `testuser2` con borrados
  comprometidos o usuario aleatorio por ejecución) fuera de esta refactor,
  como tarea separada.
- Valorar una prueba de unicidad de claves de los bundles en
  `shared-kernel` si se siguen agregando secciones i18n.
