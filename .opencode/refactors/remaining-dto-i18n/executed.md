# Executed: i18n para DTOs restantes (remaining-dto-i18n)

## Scope

Plan ejecutado: `.opencode/refactors/remaining-dto-i18n/plan.md` (pasos 1-7).
Los 4 DTOs migran de mensajes inline en inglés a claves `{dominio.campo.regla}`
resueltas por el `MessageSource` existente. No se tocó configuración i18n ni
DTOs ya migrados.

## Modified

- `user-auth/src/main/java/com/icms/user_auth/dto/user/CreateUserDto.java:13-27`
  - 9 literales reemplazados por claves `{usr.*}` según plan paso 1.
  - Regex de password, límites y `@Valid` anidado intactos.

- `user-auth/src/main/java/com/icms/user_auth/dto/userprofile/UserProfileDto.java:13-23`
  - 6 literales reemplazados por claves `{usrprof.*}` según plan paso 2.
  - `avatarUrl`, `contact` y `prefs` siguen sin anotaciones.

- `user-auth/src/main/java/com/icms/user_auth/dto/language/LanguageDto.java:10-19`
  - 7 literales reemplazados por claves `{lang.*}` según plan paso 3.
  - Regex BCP 47 intacto.

- `user-auth/src/main/java/com/icms/user_auth/dto/permission/PermissionDto.java:10-15`
  - 4 literales reemplazados por claves `{perm.*}` según plan paso 4.
  - Comentario de negocio de la línea 9 conservado.

- `shared-kernel/src/main/resources/i18n/messages.properties:74-105`
  - 26 claves en inglés agregadas al final, en 4 secciones
    (`CreateUser`, `UserProfile`, `Language`, `Permission`).
  - Nota: `usr.email.format=Invalid email format` normaliza el literal previo
    `"invalid format"`; ningún test previo asertaba ese literal.

- `shared-kernel/src/main/resources/i18n/messages_es.properties:74-105`
  - 26 claves en español agregadas al final, mismas 4 secciones.

- `user-auth/src/test/java/com/icms/user_auth/dto/user/CreateUserDtoValidationIT.java`
  - Nuevo, 18 tests (9 reglas × 2 idiomas), patrón del IT de referencia
    (`Validator` inyectado, `Locale.US` / `Locale.of("es", "ES")`,
    `@SuppressWarnings("null")`, estructura Arrange/Act/Assert).

- `user-auth/src/test/java/com/icms/user_auth/dto/userprofile/UserProfileDtoValidationIT.java`
  - Nuevo, 12 tests (6 reglas × 2 idiomas), mismo patrón.

- `user-auth/src/test/java/com/icms/user_auth/dto/language/LanguageDtoValidationIT.java`
  - Nuevo, 14 tests (7 reglas × 2 idiomas), mismo patrón.
  - `code pattern` usa `"XX"`, `code size` usa `"a"`, según el plan.

- `user-auth/src/test/java/com/icms/user_auth/dto/permission/PermissionDtoValidationIT.java`
  - Nuevo, 8 tests (4 reglas × 2 idiomas), mismo patrón.
  - `code pattern` usa `"ab"`, según el plan.

## Unchanged

- `shared-kernel/src/main/java/com/icms/shared/config/i18n/MessageConfig.java`
  - Sin cambios (infra existente reutilizada).

- `UserTypeDto`, `UserStatusDto`, `UserStatusTranslationDto`,
  `UserTypeTranslationDto` y sus bundles (`usrtype.*`, `usrstatus.*`,
  `usrstatustrans.*`, `usrtypetrans.*`)
  - Fuera de alcance, intactos.

- `pending.md`, `MEMORY.md` y `plan.md`
  - Sin cambios; la ejecución no los toca.

## Test results

- Compilación
  - `.\gradlew.bat :user-auth:compileJava :user-auth:compileTestJava
    :shared-kernel:compileJava` → `BUILD SUCCESSFUL`.
  - Único aviso: `warning: [options] --add-opens has no effect at compile
    time`, ruido preexistente del toolchain, no del código nuevo.

- IT nuevos (52 tests)
  - `CreateUserDtoValidationIT`: 18/18 en verde.
  - `UserProfileDtoValidationIT`: 12/12 en verde.
  - `LanguageDtoValidationIT`: 14/14 en verde.
  - `PermissionDtoValidationIT`: 8/8 en verde.

- Regresión i18n previa (28 tests)
  - `UserTypeDtoValidationIT` (8), `UserStatusDtoValidationIT` (8),
    `UserStatusTranslationDtoValidationIT` (6),
    `UserTypeTranslationDtoValidationIT` (6) → 28/28 en verde.

- `:shared-kernel:test` → en verde dentro del mismo build.

## Not done / blocked

- `UserControllerTest.testCreateUser`
  (`user-auth/src/test/java/com/icms/user_auth/controller/UserControllerTest.java:72`)
  falla con `Expected status code <200> but was <400>`, pero es
  **preexistente y ajeno a esta refactorización**:
  - Se reprodujo el mismo fallo en un worktree prístino de `HEAD`
    (`bef94f0`, sin ningún cambio del plan) contra la misma BD compartida
    de Postgres; el worktree se eliminó tras la comprobación.
  - Ningún cambio del plan puede alterar el resultado de validación: solo
    se sustituyeron textos de `message`, sin tocar regex, límites ni
    anotaciones.
  - Hipótesis más probable: fila residual `testuser2` en la BD compartida
    (el `tearDown` es `@Transactional` y sus borrados hacen rollback),
    por lo que la creación repetida devuelve 400 por duplicado.
  - No se modificó el test (regla: no tocar tests para hacerlos pasar) ni
    se improvisó fuera del plan; se reporta al orquestador para
    re-planificar (posible nuevo punto en `pending.md`).

## Decisions outside the plan

- Ninguna decisión de diseño fuera del plan. Desviación menor de orden:
  el plan lista DTOs (pasos 1-4) antes que los IT (paso 7); los IT se
  escribieron después del código porque asertan mensajes resueltos desde
  los bundles, que debían existir primero. El resultado final coincide con
  lo especificado (52 tests, patrón de referencia, `Locale.of`, sin APIs
  deprecadas).
