PASS

# Resultado de validación: test-naming-standard

- Slug: `test-naming-standard`
- Plan: `.opencode/refactors/test-naming-standard/plan.md`
- Ejecución: `.opencode/refactors/test-naming-standard/executed.md`
- Fecha: 2026-10-08

## Resumen por módulo

Comando: `.\gradlew.bat test --console=plain`.

| Módulo | Tests | Fallos | Línea base | Comparación |
| --- | --- | --- | --- | --- |
| `user-auth` | 130 | 1 | 130 / 1 | Igual |
| `api` | 4 | 0 | 4 / 0 | Igual |
| `shared-kernel` | 39 | 0 | 39 / 0 | Igual |
| **Total** | **173** | **1** | **173 / 1** | **Igual** |

Cero rojos nuevos. El único fallo es el preexistente P-31 (ver
"Preexistentes" más abajo).

## Verificaciones

### 1. Suite completa

- Resultado: **OK**. `.\gradlew.bat test --console=plain` ejecutó las tres
  tareas `test`; salida literal de Gradle: `130 tests completed, 1 failed` en
  `:user-auth:test`, sin fallos en `:api:test` ni `:shared-kernel:test`.
- Evidencia por artefacto: los 33 XML de `*/build/test-results/test/` suman
  173 tests y 1 fallo (recuento propio sobre `TEST-*.xml`).
- Comparación con la línea base (173 / 1): idéntica, tanto en recuento como
  en identidad del fallo.

### 2. Descubrimiento por nombre (33 clases)

- Resultado: **OK**. Los 33 XML regenerados llevan el FQCN nuevo, lo que
  demuestra que las 33 clases renombradas se descubrieron y ejecutaron:
  33 ficheros `TEST-*.xml`, p. ej.
  `build/test-results/test/TEST-com.icms.user_auth.controller.UserControllerIT.xml`,
  `TEST-com.icms.api.ApiApplicationIT.xml`,
  `TEST-com.icms.api.userauth.UserAuthGatewayRoutingIT.xml`.
- Confirmación técnica: `buildSrc/src/main/kotlin/java-common-conventions.gradle.kts:52-54`
  es `tasks.named<Test>("test") { useJUnitPlatform() }` sin filtro por
  nombre; ninguna convención filtra por sufijo.
- Conteo por clase (16 `*Test` + 17 `*IT` = 33) coincide con el inventario
  del plan.

### 3. Las 22 líneas `--tests`

Resultado: **OK**. Ninguna línea devolvió
`No tests found for given includes`.

`commands/user-auth-tests.md` (13 líneas, `:14-26`):

| Línea | Patrón | Exit | Detalle |
| --- | --- | --- | --- |
| 14 | `com.icms.user_auth.controller.TestControllerIT` | 0 | Verde |
| 15 | `com.icms.user_auth.mappers.LanguageMapperTest.createEntityFromDto` | 0 | Verde |
| 16 | `com.icms.user_auth.service.*` | 0 | Verde |
| 17 | `com.icms.user_auth.repository.LanguageRepositoryIT` | 0 | Verde |
| 18 | `com.icms.user_auth.rules.UserStatusTranslationRulesTest` | 0 | Verde |
| 19 | `com.icms.user_auth.controller.UserAuthGlobalExceptionsIT` | 0 | Verde |
| 20 | `com.icms.user_auth.dto.*` | 0 | Verde |
| 21 | `com.icms.user_auth.i18n.MessagesI18nIT` | 0 | Verde |
| 22 | `com.icms.user_auth.UserAuthApplicationIT` | 0 | Verde |
| 23 | `com.icms.user_auth.controller.*` | 1 | 4 tests / 1 fallo = P-31; sin `No tests found` |
| 24 | `com.icms.user_auth.rules.*` | 0 | Verde |
| 25 | `com.icms.user_auth.mappers.*` | 0 | Verde |
| 26 | `com.icms.user_auth.repository.*` | 0 | Verde |

`commands/api-tests.md` (4 líneas, `:16-19`): todas con exit 0.

| Línea | Patrón | Exit |
| --- | --- | --- |
| 16 | `com.icms.api.userauth.UserAuthGatewayRoutingIT` | 0 |
| 17 | `com.icms.api.userauth.UserAuthGatewayRoutingIT.testGatewayRoutingToUserAuth` | 0 |
| 18 | `com.icms.api.userauth.*` | 0 |
| 19 | `com.icms.api.ApiApplicationIT` | 0 |

`commands/shared-kernel-tests.md` (5 líneas, `:15-19`): todas con exit 0.

| Línea | Patrón | Exit |
| --- | --- | --- |
| 15 | `com.icms.shared.i18n.MessageResolverLocaleTest` | 0 |
| 16 | `com.icms.shared.i18n.MessageResolverLocaleTest.resolvesEnglishForUsLocale` | 0 |
| 17 | `com.icms.shared.i18n.MessagesBundleStructureTest` | 0 |
| 18 | `com.icms.shared.i18n.MessagesBundleCoverageTest` | 0 |
| 19 | `com.icms.shared.i18n.*` | 0 |

Las 8 líneas restantes se relanzaron además en primer plano, una por una
(`commands/api-tests.md:16-19` y `commands/shared-kernel-tests.md:15-19`),
con el mismo resultado: exit 0 y sin `No tests found for given includes`.

### 4. Coherencia de nombres (33 archivos)

- Resultado: **OK**. Script sobre `user-auth/src/test`, `api/src/test` y
  `shared-kernel/src/test`: 33 ficheros, 0 discrepancias.
  - Clase declarada = nombre de fichero en 33/33 (regex
    `^\s*(public\s+)?(final\s+)?class\s+(\w+)`).
  - Sufijos: 16 `*Test` y 17 `*IT`.
  - Cero `*IntegrationTest`, `*RepositoryTest`, `*ApplicationTests` ni `*UT`.
- Evidencia complementaria: `git status --porcelain` muestra los 9 renombres
  como `RM` (p. ej. `UserControllerTest.java -> UserControllerIT.java`).

### 5. Barrido de los 9 nombres viejos fuera de `.opencode/refactors/`

- Resultado: **OK**, 0 coincidencias. Dos métodos independientes:
  - `git grep -n -E "<9 nombres>" -- . ":!.opencode/refactors"` → exit 1
    (cero matches en tracked).
  - Barrido de disco sobre `*.md`, `*.java`, `*.kts`, `*.gradle`, `*.yml`,
    `*.yaml` excluyendo `.opencode/refactors`, `build/` y `.git/`:
    238 ficheros escaneados, `HITS_OUTSIDE_REFACTORS=0` (incluye ficheros no
    trackeados como `commands/api-tests.md` y
    `commands/shared-kernel-tests.md`).
- Mención permitida confirmada: `.github/copilot-instructions.md:83` solo
  contiene los sufijos **prohibidos** (`*UT`, `*IntegrationTest`,
  `*RepositoryTest`, `*ApplicationTests`), ninguna de las 9 clases por su
  nombre completo.

### 6. Fuente de verdad §I

- Resultado: **OK**. `.github/copilot-instructions.md:83` declara exactamente
  2 sufijos: `*Test` (unitaria) e `*IT` (integración), con la lista de
  prohibidos y los ejemplos conformes
  (`UserControllerIT`, `LanguageRepositoryIT`,
  `CreateUserDtoValidationIT`, `UserAuthApplicationIT`).
- Patrones coherentes en el resto de §I:
  `.github/copilot-instructions.md:87` (`repository/*IT.java`), `:88`
  (`controller/*IT.java`), `:89` (`*IT`), `:91` (`api/src/test/.../*IT.java`).
- Coincide con `1guides/11-pruebas.md:10-18` (columna `Sufijo` con solo
  `*Test`/`*IT`) y con la nota `1guides/11-pruebas.md:20-23`
  (`useJUnitPlatform()` sin filtro, citando
  `java-common-conventions.gradle.kts:52-54`).
- Coincide con el código real: 33/33 clases solo con `*Test`/`*IT`.

### 7. markdownlint (v0.41.1, `.markdownlint.json`)

- Resultado: **OK con 1 error preexistente**, ver detalle abajo.
- Comando: `npx --yes markdownlint-cli "<ficheros tocados>"`
  (`markdownlint-cli@0.49.1` declara `markdownlint ~0.41.1`, la versión del
  proyecto; config `.markdownlint.json` con `MD041`, `MD013` y `MD060` en
  `false`).
- 0 warnings en: `1guides/11-pruebas.md`, `1guides/10-gateway-tests.md`,
  `commands/api.md`, `commands/api-tests.md`, `commands/shared-kernel.md`,
  `commands/shared-kernel-tests.md`, `commands/user-auth-tests.md`,
  `commands/gradle.md`, `pending.md`, `MEMORY.md`,
  `.opencode/refactors/test-naming-standard/plan.md` y `executed.md`.
- Único error del lote: `.github/copilot-instructions.md:125`
  `MD025/single-title` por el encabezado `# 4. Buenas Prácticas Adicionales`.
  Es **preexistente**: relintando la versión de `HEAD`
  (`git show HEAD:.github/copilot-instructions.md`) aparece el mismo error en
  su línea 124; la refactorización solo editó `:83-91`, que pasan limpios.

### 8. No regresión de contenido (`pending.md` / `MEMORY.md`)

- Resultado: **OK**. `git diff -- pending.md MEMORY.md` muestra solo
  nombres y rutas:
  - `pending.md:71`, `:179`, `:182`, `:293` → rutas/prosa
    `UserControllerTest` a `UserControllerIT`.
  - Encabezados intactos: P-23 en `pending.md:67`, P-27 en `pending.md:176`
    y P-31 en `pending.md:290`.
  - `MEMORY.md:92` (`MessagesI18nIntegrationTest` a `MessagesI18nIT`) y
    `MEMORY.md:123` (`UserControllerTest` a `UserControllerIT`); el resto del
    texto de ambos bloques sin cambios (el diff confirma que solo cambiaron
    esas 6 líneas).

## Preexistentes

- `UserControllerIT.testCreateUser` — `user-auth/src/test/java/com/icms/user_auth/controller/UserControllerIT.java:72`
  (`java.lang.AssertionError: Expected status code <200> but was <400>`).
  Es el fallo de línea base registrado como P-31 en `pending.md:290`; tras el
  renombre R1 pasó de llamarse `UserControllerTest.testCreateUser`. No es un
  FAIL de esta refactorización: mismo test, misma línea, mismo mensaje.
- `MD025` en `.github/copilot-instructions.md:125` — preexistente (igual en
  `HEAD` línea 124), ajeno a §I y a esta refactorización.

## No verificado

- Ninguno. Todas las comprobaciones de la lista se ejecutaron; no hubo
  restricciones de permisos en esta fase (a diferencia de la fase de
  ejecución, donde `executed.md` dejó markdownlint pendiente: queda
  resuelto arriba en la verificación 7).

## Opcional

No bloquean ni incumplen el plan:

1. `.github/copilot-instructions.md:125` — corregir el `MD025` preexistente
   (segundo H1 `# 4. Buenas Prácticas Adicionales`) para dejar el documento
   en 0 errores de markdownlint.
2. `commands/user-auth.md:44`, `commands/user-auth-tests.md:9` y
   `commands/api.md:30` — prosa heredada "Ejecuta todos los `Test` del
   modulo" (sin tilde y con `Test` en lugar de "tests"): el plan no la
   incluía, pero puede unificarse con el resto de la documentación.
