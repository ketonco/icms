# Ejecución: `messages-properties-usecases`

- **Slug:** `messages-properties-usecases`
- **Plan base:** `.opencode/refactors/messages-properties-usecases/plan.md`
- **Fecha de ejecución:** 2026-10-07
- **Estado:** Ciclo 1 y Ciclo 2 ejecutados el 2026-10-07. El bloqueo de
  `MessageResolver` del Ciclo 1 quedó resuelto con O5; el único fallo
  restante es el preexistente P-31 (O6, fuera de alcance). Ver
  [Ciclo 2](#ciclo-2-2026-10-07) y
  [Estado final del Definition of Done](#estado-final-del-definition-of-done).

Este documento registra qué se modificó, qué quedó igual, qué no se pudo
hacer y los resultados de las pruebas de la Fase 5.

## Qué se modificó

### Fase 0: dependencias de prueba y red de tests en rojo

- `shared-kernel/build.gradle:33-35` — se agregaron `testImplementation
  libs.spring.boot.starter.test` y `testRuntimeOnly libs.junit.platform`
  (decisión O3; `gradle/libs.versions.toml` sin tocar).
- `shared-kernel/src/test/java/com/icms/shared/i18n/MessagesBundleStructureTest.java`
  — clase nueva (T1, 13 tests).
- `shared-kernel/src/test/java/com/icms/shared/i18n/MessagesBundleCoverageTest.java`
  — clase nueva (T2, 2 tests).
- `shared-kernel/src/test/java/com/icms/shared/i18n/MessageResolverLocaleTest.java`
  — clase nueva (T3, 24 tests).
- `user-auth/src/test/java/com/icms/user_auth/i18n/MessagesI18nIntegrationTest.java`
  — clase nueva (T4, 5 tests).
- `user-auth/src/test/java/com/icms/user_auth/rules/LanguageRulesTest.java`
  — clase nueva (T5, 5 tests).

El rojo inicial quedó registrado antes de tocar producción: T1 fallaba por
numeración (`E-000`, `S-000`), comentarios (`# Success `,
`#UserProfile Validation`) y orden de validaciones; T2 fallaba con las 15
claves huérfanas; T3 y T4 fallaban en todos los códigos renumerados.

### Fase 1: reescritura atómica de los bundles

- `shared-kernel/src/main/resources/i18n/messages.properties:1-92` —
  reescrito entero con el Anexo A del plan (61 claves: 21 de negocio y 40
  de validación, antes 76).
- `shared-kernel/src/main/resources/i18n/messages_es.properties:1-92` —
  reescrito entero con el Anexo B, espejo línea a línea (mismas 92 líneas,
  mismos comentarios en las mismas posiciones, verificado por lectura).

Ambos archivos terminan con exactamente un salto de línea final (antes el
archivo EN terminaba con salto y el ES sin él).

### Fase 2: 16 literales de negocio en código

- `shared-kernel/.../controller/WriteController.java:16` — `S-000` → `S-001`.
- `shared-kernel/.../controller/UpdateController.java:16` — `S-001` → `S-002`.
- `shared-kernel/.../controller/DeleteController.java:15` — `S-002` → `S-003`.
- `shared-kernel/.../config/exception/GlobalExceptionHandler.java:42` —
  `E-000` → `E-001` y comentario en español → `// Business rule error code`
  (decisión O4).
- `shared-kernel/.../rules/BaseDaoRules.java:35` — `Ent-004` → `Ent-003`.
- `shared-kernel/.../rules/BaseDaoRules.java:41` — `Ent-003` → `Ent-002`.
- `shared-kernel/.../rules/BaseDaoCatalogTranslationRules.java:36` —
  `Lan-007` → `Lan-002`.
- `shared-kernel/.../rules/BaseDaoCatalogTranslationRules.java:47` —
  `Lan-006` → `Lan-003`.
- `user-auth/.../rules/dao/LanguageRules.java:28` — `Ent-006` → `Ent-005`.
- `user-auth/.../rules/dao/LanguageRules.java:34` — `Ent-005` → `Ent-004`.
- `user-auth/.../rules/dao/LanguageRules.java:41` — `Lan-008` → `Lan-001`.
- `user-auth/.../rules/dao/UserRules.java:27` — `Usr-010` → `Usr-002`.
- `user-auth/.../rules/dao/UserRules.java:34` — `Usr-011` → `Usr-003`.
- `user-auth/.../rules/dao/UserProfileRules.java:47` — `UsrProf-009` →
  `UsrProf-002`.
- `user-auth/.../rules/dao/UserProfileRules.java:64` — `UsrProf-010` →
  `UsrProf-003`.
- `user-auth/.../service/daoservice/UserProfileService.java:55` — `Usr-009`
  → `Usr-001`.

Verificación: una búsqueda en todo el repositorio de `S-000`, `E-000`,
`Usr-009`, `Usr-010`, `Usr-011`, `UsrProf-009`, `UsrProf-010`, `Lan-006`,
`Lan-007` y `Lan-008` no devuelve ninguna coincidencia en `src/main`.

### Fase 3: aserciones de tests existentes

- `user-auth/src/test/.../rules/UserProfileRulesTest.java:78` y `:80` —
  `UsrProf-009` → `UsrProf-002`.
- `user-auth/src/test/.../rules/UserProfileRulesTest.java:125` y `:127` —
  `Ent-003` → `Ent-002`.
- `user-auth/src/test/.../rules/UserRulesTest.java:76` y `:78` — `Usr-010`
  → `Usr-002`.
- `user-auth/src/test/.../rules/UserRulesTest.java:98` y `:100` — `Usr-011`
  → `Usr-003`.
- `user-auth/src/test/.../rules/UserStatusTranslationRulesTest.java:96` y
  `:98` — `Lan-007` → `Lan-002`.
- `user-auth/src/test/.../service/UserProfileServiceTest.java:109` y
  `:111` — `Usr-009` → `Usr-001`.
- `user-auth/src/test/.../service/UserStatusTranslationServiceTest.java:70`
  y `:76` — `Lan-007` → `Lan-002`.

Son las 7 aserciones enumeradas en la sección 4 del plan (8 contando
`UserStatusTranslationServiceTest`). No se tocó ninguna otra aserción.

## Qué quedó igual

- Los 40 DTOs de `user-auth`: 0 archivos tocados; ninguna clave
  `{dominio.campo.regla}` cambió de nombre.
- `shared-kernel/.../config/i18n/MessageConfig.java` y
  `shared-kernel/.../Utils/MessageResolver.java`: sin cambios.
- Migraciones Liquibase y esquema: sin cambios.
- Literales ya correctos: `BaseService.java:111,120` (`Ent-001`),
  `EntityNotFoundException.java:5`, `BaseDaoCatalogRules.java:30,38`,
  `UserService.java:48,52` (`Cat-001`), `UserProfileService.java:56`,
  `UserProfileRules.java:58` (`UsrProf-001`).
- `UserProfileRulesTest.java:145,147` (`Ent-001`) y
  `UserProfileServiceTest.java:133,135` (`UsrProf-001`): sin tocar.
- `UserAuthGlobalExceptionsTest.java:57,87` (`Ent-001`, `Cat-002`): sin
  tocar y en verde.
- Los 8 `*DtoValidationIT`: sin tocar y en verde.
- `gradle/libs.versions.toml`: sin tocar (el alias de O3 ya existía).

## No se pudo hacer

- **`no se pudo:` T4 parcialmente bloqueada — 4 de 5 métodos de
  `MessagesI18nIntegrationTest` no pueden ponerse en verdes.**
  Resuelto en el Ciclo 2 con O5 (registro de `MessageResolver` como
  `@Bean`), ver [Ciclo 2](#ciclo-2-2026-10-07). Causa en su momento:
  `MessageResolver` nunca se registra como bean Spring en `user-auth`: la
  clase no está en el escaneo de `@SpringBootApplication`
  (escanea solo `com.icms.user_auth`) ni en el
  `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
  de `shared-kernel`. Su campo estático `messageSource` queda a `null`, así
  que todo mensaje HTTP es `"<code> context"`. Evidencia en
  `user-auth/build/test-results/test/TEST-com.icms.user_auth.i18n.MessagesI18nIntegrationTest.xml`:
  `Actual: Ent-001 context` (esperado `Entidad no encontrada.`),
  `Actual: Cat-002 context` y `Actual: S-001 context`. Registrar el bean
  exige tocar archivos que el plan declara sin cambios, así que se reporta
  en vez de improvisar. Afecta a:
  `entityNotFoundReturnsSpanishMessage()` (`:87`),
  `entityNotFoundReturnsEnglishMessage()` (`:103`),
  `duplicateCatalogCodeReturnsSpanishMessage()` (`:128`) y
  `createSuccessReturnsRenamedCode()` (`:152`).
  `everyBusinessCodeResolvesInRealMessageSource()` sí pasa porque usa el
  `MessageSource` del contexto, no `MessageResolver`.
- **`no se pudo:` Fase 4 fuera de alcance por permisos, asignada al
  orquestador.** No se modificaron `.github/copilot-instructions.md` ni
  `pending.md` desde este executor. Al verificar solo lectura, el estado
  objetivo ya está cumplido: `copilot-instructions.md:64-65` describe el
  estándar (`001..NNN` contiguo por caso de uso y paridad ES), la línea
  correspondiente a la antigua `:77` ya lista `S-001`/`S-002`/`S-003`
  (`:78`) y `pending.md:231` (P-29) y `pending.md:260` (P-30) existen.
- **Fallo preexistente no causado por esta refactorización:**
  `UserControllerTest.testCreateUser` espera 200 y recibe 400 por una fila
  antigua en la tabla `users` (`testuser2@example.com`) más un defecto de
  rollback del tearDown; verificado con solo lectura en la BD de test
  antes de empezar (línea base: 120 tests, 1 fallo, mismo fallo).

## Decisiones que el plan no cubría

- `MessagesBundleCoverageTest.repositoryRoot()` acepta tanto
  `settings.gradle` como `settings.gradle.kts`, porque la raíz del
  repositorio solo tiene `settings.gradle` y el plan (riesgo R5) nombraba
  únicamente `.kts`.
- Se añadieron dos métodos a `MessagesBundleStructureTest` más allá de la
  lista T1: `sectionsFollowTheStandardOrder()` y
  `sectionCommentsFollowStandardFormat()`, porque la Fase 0 exige rojo
  también sobre el orden de secciones y el formato de los comentarios
  (H4/H5), que T1 en su lista base no afirmaba.
- Se retiró el over-assertion propio
  `verify(never()).findByIsDefault(true)` de
  `LanguageRulesTest.canDeleteAllowsNonDefaultLanguage()`: el plan solo
  pide "sin excepción" y `LanguageRules.checkIsDefault` invoca
  `findByIsDefault` de forma incondicional.
- Los dos bundles quedan con exactamente un salto de línea final cada uno;
  la paridad EN/ES exigía normalizarlo (el ES no terminaba con salto).
- Ambas clases nuevas comparten JVM de pruebas en `shared-kernel`, aislada
  de `user-auth`, por lo que el estado estático de `MessageResolver` no se
  cruza (R4 confirmado en la práctica).

## Resultados de pruebas (Ciclo 1)

| Comando                                | Resultado                                                     |
| -------------------------------------- | ------------------------------------------------------------- |
| `gradlew.bat :shared-kernel:test`      | BUILD SUCCESSFUL — 39 tests, 0 fallos (13 + 2 + 24)           |
| `gradlew.bat :user-auth:test`          | 130 tests, 5 fallos (4 de T4 bloqueadas + 1 preexistente)     |
| `gradlew.bat test --continue`          | `:api` y `:shared-kernel` en verde; `:user-auth` 130/5 fallos |
| `gradlew.bat build --continue`         | Compilación, jar y assemble en verde en los 3 módulos; solo falla `:user-auth:test` |

Desglose de los 5 fallos de `user-auth`:

- 4 en `MessagesI18nIntegrationTest` (bloqueo de `MessageResolver`, ver
  arriba): `:87`, `:103`, `:128`, `:152`.
- 1 preexistente en `UserControllerTest:72` (datos de BD de test,
  reproducido en la línea base antes de cualquier cambio).

En verde: `LanguageRulesTest` (5/5), `MessagesBundleStructureTest`,
`MessagesBundleCoverageTest`, `MessageResolverLocaleTest` (39/39 en
`shared-kernel`), los 8 `*DtoValidationIT`, `UserAuthGlobalExceptionsTest`
y los 5 tests existentes actualizados en la Fase 3.

## Ciclo 2 (2026-10-07)

Ejecución de las Fases 1 a 4 del plan (Ciclo 2 aprobado) con las
decisiones O5, O6 y O7 opción A.

### Qué se modificó en el Ciclo 2

#### Fase 1: `@Bean` de `MessageResolver` (O5)

- `shared-kernel/src/main/java/com/icms/shared/config/i18n/MessageConfig.java:3`
  — import nuevo `com.icms.shared.Utils.MessageResolver` como primer
  import, tras la línea en blanco.
- `shared-kernel/src/main/java/com/icms/shared/config/i18n/MessageConfig.java:27-30`
  — método nuevo `messageResolver(MessageSource)` con `@Bean`, entre el
  cierre de `messageSource()` y la llave de la clase, con la inyección
  por tipo que exige O5. Sin imports sobrantes y `messageSource()` sin
  cambios (verificado por lectura).

#### Fase 2: limpieza de la fila creada por T4

- `user-auth/src/test/java/com/icms/user_auth/i18n/MessagesI18nIntegrationTest.java:7-8`
  — imports nuevos `com.icms.user_auth.repository.LanguageRepository` y
  `org.junit.jupiter.api.AfterEach`.
- `user-auth/src/test/.../i18n/MessagesI18nIntegrationTest.java:70-74`
  — campos nuevos `@Autowired LanguageRepository languageRepository` y
  `private String createdLanguageCode`.
- `user-auth/src/test/.../i18n/MessagesI18nIntegrationTest.java:82-91`
  — método nuevo `@AfterEach deleteCreatedLanguage()` que borra la fila
  si el código quedó guardado y anula el campo.
- `user-auth/src/test/.../i18n/MessagesI18nIntegrationTest.java:152-163`
  — `createSuccessReturnsRenamedCode` extrae `code` y `name` a
  variables locales, asigna `createdLanguageCode = code;` antes del
  `POST` (`:155`) y los pasa a `formatted` (`:163`), de modo que la
  limpieza funciona aunque el `POST` o el aserto fallen.
- `user-auth/src/test/.../i18n/MessagesI18nIntegrationTest.java:209-217`
  — `randomLanguageCode` sube de 2 a 3 minúsculas dentro del patrón
  `^[a-z]{2,3}(-[A-Z]{2})?$` (entropía de 456 976 a 11 881 376
  combinaciones).

#### Fase 3: aislamiento de las 12 aserciones (O7, opción A)

Borradas las 11 líneas `.hasMessage("<código> context")`, conservando
`.isInstanceOf(...)` y `.extracting("code")`:

- `user-auth/src/test/.../rules/LanguageRulesTest.java:79`, `:110` y
  `:128` (`Lan-001`, `Ent-004`, `Ent-005`).
- `user-auth/src/test/.../rules/UserProfileRulesTest.java:78`, `:124` y
  `:143` (`UsrProf-002`, `Ent-002`, `Ent-001`).
- `user-auth/src/test/.../rules/UserRulesTest.java:76` y `:97`
  (`Usr-002`, `Usr-003`).
- `user-auth/src/test/.../rules/UserStatusTranslationRulesTest.java:96`
  (`Lan-002`).
- `user-auth/src/test/.../service/UserProfileServiceTest.java:109` y
  `:132` (`Usr-001`, `UsrProf-001`).

Y sustituida la aserción 12:

- `user-auth/src/test/.../service/UserStatusTranslationServiceTest.java:76`
  — `assertEquals("Lan-002 context", e.getMessage())` por
  `assertEquals("Lan-002", e.getCode())`.

Verificación: la búsqueda literal `" context"` en `user-auth/src/test`
no devuelve ninguna coincidencia. No se tocó ninguna otra aserción de
esas 6 clases.

### Qué quedó igual en el Ciclo 2

- `shared-kernel/src/main/java/com/icms/shared/Utils/MessageResolver.java`,
  `user-auth/src/main/java/com/icms/user_auth/UserAuthApplication.java`
  y `AutoConfiguration.imports`: intactos, como exige O5.
- `user-auth/src/test/.../controller/UserControllerTest.java` sin tocar
  (O6, fallo preexistente P-31).
- Los dos bundles, los 16 literales del Ciclo 1, los 40 DTOs,
  `gradle/libs.versions.toml` y las migraciones Liquibase: sin cambios.
- `MessageConfig.java` es el único archivo de `src/main` modificado en
  el Ciclo 2.
- Documentación (`AGENTS.md`, `.github/**`, `MEMORY.md`, `pending.md`,
  `ROADMAP.md`, `.opencode/agents/**`): sin tocar por este executor.

### Decisiones no cubiertas por el plan (Ciclo 2)

- Posición de los dos imports nuevos en
  `MessagesI18nIntegrationTest.java`: tras el bloque `java.*`, en el
  grupo siguiente con orden alfabético; el plan exigía los imports pero
  no fijaba su ubicación.
- Comentarios `/* ... */` propios sobre `createdLanguageCode` y
  `deleteCreatedLanguage()`, en el estilo de comentarios que la clase
  ya usaba; el plan solo daba el código del método.

### No se pudo hacer en el Ciclo 2

- **`no se pudo:` ejecutar la comprobación automática de markdownlint**
  sobre `.opencode/refactors/*.md`: el shell de este executor está
  limitado a `gradlew`/`gradlew.bat` y el repositorio no incluye
  configuración de markdownlint. Los `.md` de este slug se escribieron
  cumpliendo a mano las reglas (líneas en blanco alrededor de
  encabezados y listas, sin saltos de nivel de encabezado).
- El fallo P-31 (`UserControllerTest.java:72`) sigue en rojo por
  decisión O6: no es un pendiente de este ciclo.

### Resultados de pruebas (Ciclo 2)

| Comando                           | Resultado |
| --------------------------------- | --------- |
| `gradlew.bat :shared-kernel:test` | BUILD SUCCESSFUL — 39 tests, 0 fallos (13 + 2 + 24) |
| `gradlew.bat :user-auth:test`     | 130 tests, 1 fallo (`UserControllerTest.testCreateUser`, P-31); 129 en verde |
| `gradlew.bat build --continue`    | Compilación, `jar`, `bootJar` y `assemble` en verde en los 3 módulos; BUILD FAILED solo por el fallo P-31 de `:user-auth:test` |

Desglose del Ciclo 2:

- `MessagesI18nIntegrationTest` 5 de 5 en verde con los textos
  reales: `Entidad no encontrada.` (es-ES), `Entity not found.` (en-US),
  `El código del catálogo debe ser único.` (400) y
  `Entidad creada con éxito` (200), además de la aserción de los 21
  códigos sobre el `MessageSource` del contexto.
- Las 6 clases de O7 verdes con sus aserciones de `code`:
  `LanguageRulesTest` 5, `UserProfileRulesTest` 5, `UserRulesTest` 3,
  `UserStatusTranslationRulesTest` 2, `UserProfileServiceTest` 4 y
  `UserStatusTranslationServiceTest` 2.
- Fase 4.4, consulta de solo lectura a la BD de pruebas tras las
  corridas de 17:33 y 17:34: **cero filas nuevas** en `languages`; la
  última fila residual es de 16:52 (corridas del Ciclo 1). El
  `@AfterEach` con la entropía subida funciona; las 4 filas residuales
  previas quedan fuera de alcance.
- El único fallo de `:user-auth:test` sigue siendo
  `UserControllerTest.testCreateUser` en `:72`, reproducido en la línea
  base antes de empezar.

## Estado final del Definition of Done

- [x] `gradlew.bat :shared-kernel:test` en verde: 39 tests y 0 fallos.
- [x] `gradlew.bat :user-auth:test` con 129 de 130 tests en verde; el
  único fallo admisible es `UserControllerTest.testCreateUser` (P-31,
  O6, fuera de alcance).
- [x] `MessagesI18nIntegrationTest` 5 de 5 con los textos reales EN/ES
  y sin filas nuevas en `languages` tras cada corrida (verificado por
  consulta de solo lectura).
- [x] `gradlew.bat build --continue` sin errores de compilación, de jar
  ni de assemble en `:api`, `:shared-kernel` y `:user-auth`; el único
  rojo es `:user-auth:test` con el fallo P-31.
- [x] Las 12 aserciones de fallback sustituidas por aserciones de
  `code` (O7 opción A), con T3 cubriendo el contrato de fallback.
- [x] `MessageResolver.java`, `UserAuthApplication.java` y
  `AutoConfiguration.imports` intactos; `MessageConfig.java` es el único
  archivo de `src/main` modificado en el Ciclo 2.
- [x] Fase 4 documental del Ciclo 1 (`copilot-instructions.md` y
  `pending.md` P-29/P-30) y P-31: aplicadas fuera de este executor por
  el orquestador.
- [x] Markdown de este documento con espacios alrededor de encabezados
  y listas y sin saltos de nivel (verificado a mano; la comprobación
  automática de markdownlint quedó fuera de alcance).
