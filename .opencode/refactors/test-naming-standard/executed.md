# Ejecución: test-naming-standard

Cierre de la fase de ejecución en segunda pasada. Este documento cubre los
cambios aplicados por el executor en la primera pasada, los aplicados por el
orquestador sobre los archivos denegados y la validación final de la suite.

- Slug: `test-naming-standard`
- Plan: `.opencode/refactors/test-naming-standard/plan.md`
- Estándar: 2 sufijos, `*Test` (unitaria) e `*IT` (integración)

## Renombres ejecutados (9)

Cada renombre es `git mv` del archivo más la edición de su declaración de
clase en la misma línea (el número de línea no cambió):

| # | Archivo nuevo | Declaración | Nombre anterior |
| --- | --- | --- | --- |
| R1 | `user-auth/src/test/java/com/icms/user_auth/controller/UserControllerIT.java` | `:23` | `UserControllerTest` |
| R2 | `user-auth/src/test/java/com/icms/user_auth/controller/UserAuthGlobalExceptionsIT.java` | `:19` | `UserAuthGlobalExceptionsTest` |
| R3 | `user-auth/src/test/java/com/icms/user_auth/controller/TestControllerIT.java` | `:13` | `TestControllerIntegrationTest` |
| R4 | `user-auth/src/test/java/com/icms/user_auth/i18n/MessagesI18nIT.java` | `:36` | `MessagesI18nIntegrationTest` |
| R5 | `user-auth/src/test/java/com/icms/user_auth/repository/LanguageRepositoryIT.java` | `:21` | `LanguageRepositoryTest` |
| R6 | `user-auth/src/test/java/com/icms/user_auth/repository/UserProfileRepositoryIT.java` | `:28` | `UserProfileRepositoryTest` |
| R7 | `user-auth/src/test/java/com/icms/user_auth/UserAuthApplicationIT.java` | `:7` | `UserAuthApplicationTests` |
| R8 | `api/src/test/java/com/icms/api/ApiApplicationIT.java` | `:7` | `ApiApplicationTests` |
| R9 | `api/src/test/java/com/icms/api/userauth/UserAuthGatewayRoutingIT.java` | `:25` | `UserAuthGatewayRoutingIntegrationTest` |

Cambio adicional dentro de R4: el Javadoc de
`user-auth/src/test/java/com/icms/user_auth/i18n/MessagesI18nIT.java:28`
referenciaba `{@code UserAuthGlobalExceptionsTest}` y quedó como
`{@code UserAuthGlobalExceptionsIT}`.

## Documentación actualizada por el executor

Todos los cambios son de nombres, rutas o patrones de sufijo; la prosa
técnica de las guías quedó intacta.

- `1guides/11-pruebas.md:10-18` — la tabla gana la columna `Sufijo` y los
  ejemplos pasan a los nombres nuevos (`LanguageRepositoryIT` en `:14`,
  `UserAuthGlobalExceptionsIT` en `:16`, `MessagesI18nIT` en `:17`,
  `UserAuthGatewayRoutingIT` en `:18`).
- `1guides/11-pruebas.md:20-23` — la nota de ejecución se reescribe para el
  estándar de dos sufijos y cita
  `buildSrc/src/main/kotlin/java-common-conventions.gradle.kts:52-54`.
- `1guides/11-pruebas.md:227` — ejemplos `LanguageRepositoryIT` y
  `UserProfileRepositoryIT`.
- `1guides/11-pruebas.md:262-263` — `UserControllerIT`,
  `UserAuthGlobalExceptionsIT` y `TestControllerIT`.
- `1guides/11-pruebas.md:271` — `MessagesI18nIT`.
- `1guides/11-pruebas.md:293` — `ApiApplicationIT` y `UserAuthApplicationIT`.
- `1guides/10-gateway-tests.md:5` — ruta del archivo a
  `UserAuthGatewayRoutingIT.java`.
- `1guides/10-gateway-tests.md:60` — comando `--tests` con el FQCN nuevo.
- `commands/api-tests.md:1-23` — creado: suite completa de `api` y 4 líneas
  `--tests` con nombres nuevos.
- `commands/shared-kernel-tests.md:1-23` — creado: suite completa de
  `shared-kernel` y 5 líneas `--tests`.
- `commands/api.md:24-32` — sección "Ejecutar todos los tests" con remisión a
  `api-tests.md`; el bloque "Pruebas puntuales (`--tests`)" que ocupaba
  `:34-41` se movió sin duplicarlo a `commands/api-tests.md`.
- `commands/shared-kernel.md:19-26` — sección nueva "Ejecutar todos los
  tests" con remisión a `shared-kernel-tests.md`.
- `commands/user-auth-tests.md:14` — `TestControllerIntegrationTest` a
  `TestControllerIT`.
- `commands/user-auth-tests.md:17` — `LanguageRepositoryTest` a
  `LanguageRepositoryIT`.
- `commands/user-auth-tests.md:19` — FQCN roto
  `com.icms.user_auth.exceptions.UserAuthGlobalExceptionsIT` corregido a
  `com.icms.user_auth.controller.UserAuthGlobalExceptionsIT`.
- `commands/user-auth-tests.md:20-22` — 3 líneas nuevas: `dto.*`,
  `i18n.MessagesI18nIT` y `UserAuthApplicationIT`.
- `commands/gradle.md:59-67` — sección global "Ejecutar todos los tests"
  (`.\gradlew.bat test`) con remisión a los tres `*-tests.md`.

## Ejecutado por el orquestador

Los tres archivos siguientes están cubiertos por el `effect: deny` de
`.opencode/agents/refactor-executor.md:11-19`. El executor no los tocó; el
orquestador los aplicó con autorización explícita del desarrollador y el
deny sigue intacto (`.opencode/agents/**` también está denegado para el
executor).

- `.github/copilot-instructions.md:83` — bullet nuevo "Sufijos de nombres
  (estándar de dos)" con la dupla `*Test`/`*IT` y la lista de sufijos
  prohibidos.
- `.github/copilot-instructions.md:84` — "Principio selectivo" revisado
  (sin `*RepositoryTest` ni la excepción de patrón `*IT`).
- `.github/copilot-instructions.md:87` — patrón `repository/*IT.java`.
- `.github/copilot-instructions.md:88` — patrón `controller/*IT.java`.
- `.github/copilot-instructions.md:89` — `RestAssured (*IT)`.
- `.github/copilot-instructions.md:91` — `api/src/test/.../*IT.java`.
- `.github/copilot-instructions.md:85-86`, `:90` y `:92` — intactos
  (coinciden con `:84-85`, `:89` y `:91` del plan, con desplazamiento de una
  línea por el bullet nuevo).
- `pending.md:71`, `:179`, `:182` y `:293` — solo rutas y nombres a
  `UserControllerIT`; los encabezados P-23 (`pending.md:67`), P-27
  (`pending.md:176`) y P-31 (`pending.md:290`) quedaron intactos.
- `MEMORY.md:92` — `MessagesI18nIntegrationTest` a `MessagesI18nIT`.
- `MEMORY.md:123` — `UserControllerTest` a `UserControllerIT`.

## Ampliación autorizada posterior

Fuera del plan original y pedida por el desarrollador después de cerrarlo:

- `commands/user-auth-tests.md:23-26` — 4 líneas `--tests` con comodines:
  `com.icms.user_auth.controller.*`, `com.icms.user_auth.rules.*`,
  `com.icms.user_auth.mappers.*` y `com.icms.user_auth.repository.*`.
- `commands/shared-kernel-tests.md:17-18` — 2 líneas `--tests`:
  `com.icms.shared.i18n.MessagesBundleStructureTest` y
  `com.icms.shared.i18n.MessagesBundleCoverageTest`.

Resultado: cobertura documental de 33/33 clases de test.

## Verificación de inventario

- Glob sobre `**/src/test/java/**/*.java`: 33 archivos, 16 con sufijo
  `*Test` y 17 con sufijo `*IT`. Ningún archivo conserva el nombre viejo de
  su clase; no existe ningún `*IntegrationTest.java`, `*RepositoryTest.java`,
  `*ApplicationTests.java` ni `UserControllerTest.java` bajo `src/test`.
- Grep de la declaración de clase
  (`^\s*(public\s+)?(final\s+)?class \w+`) sobre
  `**/src/test/**/*.java`: 33 coincidencias, todas iguales al nombre de su
  archivo. Ninguna clase quedó con la declaración vieja.
- Inventario por módulo: `user-auth` 28, `api` 2, `shared-kernel` 3.

## Barrido final de referencias

Grep de los 9 nombres viejos sobre todo el repositorio: 139 coincidencias,
todas dentro de `.opencode/refactors/` (este `plan.md` y los registros
históricos). Cero coincidencias en:

- `*.java` (código fuente y tests)
- `commands/`
- `1guides/`
- `.github/`
- `pending.md` y `MEMORY.md`
- `.agents/` y `guia-temporal/`

## Resultado de la suite

Última ejecución completa: `.\gradlew.bat test --console=plain`.

| Módulo | Tests | Fallos | Detalle |
| --- | --- | --- | --- |
| `user-auth` | 130 | 1 | `UserControllerIT.testCreateUser` en `UserControllerIT.java:72` (P-31, preexistente) |
| `api` | 4 | 0 | `ApiApplicationIT` 1, `UserAuthGatewayRoutingIT` 3 |
| `shared-kernel` | 39 | 0 | `MessageResolverLocaleTest` 24, `MessagesBundleStructureTest` 13, `MessagesBundleCoverageTest` 2 |
| Total | 173 | 1 | Único rojo = línea base |

- El rojo es exactamente la línea base conocida: `UserControllerIT`
  (`UserControllerIT.java:46` el método, `:72` la asertión, 200 esperado y
  400 recibido), registrado en P-31. No apareció ningún fallo nuevo.
- La suite se ejecutó dos veces: una antes y otra después de la validación
  de las líneas `--tests`, para dejar los artefactos de
  `build/test-results/` correspondientes al suite completo.

## Validación de las líneas `--tests`

Las 22 líneas de los tres `commands/*-tests.md` se ejecutaron una por una.
Ninguna devolvió `No tests found for given includes`:

- `commands/user-auth-tests.md:14-26` — 13 líneas; 12 en verde y
  `com.icms.user_auth.controller.*` en rojo (4 tests, 1 fallo), atribuible
  al mismo P-31.
- `commands/api-tests.md:16-19` — 4 líneas, todas en verde.
- `commands/shared-kernel-tests.md:15-19` — 5 líneas, todas en verde.

## Desviaciones respecto al plan

- Se añadieron 6 líneas `--tests` después de aprobar el plan
  (`commands/user-auth-tests.md:23-26` y
  `commands/shared-kernel-tests.md:17-18`), por petición explícita del
  desarrollador y fuera del alcance original.
- La línea base "130 tests / 1 fallo" del plan corresponde a la tarea
  `:user-auth:test`. El suite completo suma 173 tests (130 + 4 + 39); el
  conteo de fallos no cambia: 1, el preexistente.
- markdownlint no se ejecutó como binario: el permiso `shell` del executor
  solo admite comandos `*gradlew*`. Este archivo se redactó con las reglas
  del repo (`.markdownlint.json`, con `MD041`, `MD013` y `MD060`
  desactivados): blancos antes y después de encabezados, listas y tablas,
  sin saltos de nivel y con separadores `| --- |`. Queda pendiente que el
  orquestador ejecute markdownlint sobre él.
- El executor no pudo ejecutar `git status` ni `git mv` por el mismo
  límite de permisos; la evidencia de los renombres es la ausencia de los
  nombres viejos en disco y la presencia de los nuevos (verificado arriba).

## Qué quedó igual y qué quedó fuera de alcance

- Las 24 clases ya conformes (16 `*Test` + 8 `*DtoValidationIT`): sin
  renombre ni edición; ninguna clase de test se creó, borró o reescribió.
- Sin cambios de cobertura, de dependencias, de `gradle/libs.versions.toml`,
  de ningún `build.gradle` ni de `buildSrc/`.
- Sin migraciones Liquibase ni cambios de esquema.
- El fallo P-31 sigue fuera de alcance y sin corregir.
- `commands/` sigue en la raíz (no se movió a `.opencode/commands/`).
- `AGENTS.md` intacto: no menciona sufijos ni clases de test.
- `.opencode/agents/**` intacto: el `effect: deny` del executor no fue
  modificado.
- La prosa preexistente "Ejecuta todos los `Test` del modulo" de
  `commands/user-auth.md:44`, `commands/user-auth-tests.md:9` y
  `commands/api.md:30` no se tocó: el plan no la incluía.
- Los XML de `**/build/test-results/` regenerados con el FQCN nuevo son
  artefacto ignorado por Git (`.gitignore:5`).

## Registros históricos no tocados

Los registros de refactorizaciones anteriores conservan sus nombres de clase
originales por ser historia y no se modificaron en ninguna pasada:

- `.opencode/refactors/messages-properties-usecases/`
- `.opencode/refactors/usertype-dto-i18n/`
- `.opencode/refactors/user-status-dto-i18n/`
- `.opencode/refactors/remaining-dto-i18n/`

Las únicas menciones de los 9 nombres viejos que quedan en el repositorio
están en esos registros y en este `plan.md`, tal como prevé el plan.
