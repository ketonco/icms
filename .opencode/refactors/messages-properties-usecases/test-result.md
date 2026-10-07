# Resultado de pruebas: `messages-properties-usecases`

- **Slug:** `messages-properties-usecases`
- **Plan base:** `.opencode/refactors/messages-properties-usecases/plan.md` (Ciclo 2, aprobado)
- **Ejecución registrada:** `.opencode/refactors/messages-properties-usecases/executed.md` (Ciclo 1 y Ciclo 2)
- **Fecha de validación:** 2026-10-07
- **Veredicto final:** **PASS**

## Tests ejecutados

Los tres comandos se ejecutaron en Windows con reejecución forzada
(`--rerun`) para no validar sobre resultados en caché
(`UP-TO-DATE`).

| Comando                            | Resultado esperado                                            | Resultado obtenido                                              |
| ---------------------------------- | ------------------------------------------------------------- | --------------------------------------------------------------- |
| `gradlew.bat :shared-kernel:test`  | BUILD SUCCESSFUL — 39 tests, 0 fallos (T1 13, T2 2, T3 24)    | BUILD SUCCESSFUL — 39 tests, 0 fallos (13 + 2 + 24) ✓           |
| `gradlew.bat :user-auth:test`      | 130 tests, 1 fallo (P-31)                                     | BUILD FAILED — 130 tests, 1 fallo, 129 en verde ✓               |
| `gradlew.bat build --continue`     | Compilación, jar y assemble en verde en `:api`, `:shared-kernel`, `:user-auth` | `compileJava`, `jar`, `bootJar` y `assemble` en verde en los 3 módulos; BUILD FAILED solo por `:user-auth:test` (P-31) ✓ |

### `:shared-kernel:test` — 39 tests, 0 fallos

| Clase                          | Tests | Fallos | Errores | Ignorados |
| ------------------------------ | ----- | ------ | ------- | --------- |
| `MessagesBundleStructureTest`  | 13    | 0      | 0       | 0         |
| `MessagesBundleCoverageTest`   | 2     | 0      | 0       | 0         |
| `MessageResolverLocaleTest`    | 24    | 0      | 0       | 0         |
| **Total**                      | **39** | **0**  | **0**   | **0**     |

### `:user-auth:test` — 130 tests, 1 fallo (P-31)

| Clase                                       | Tests | Fallos | Errores | Ignorados |
| ------------------------------------------- | ----- | ------ | ------- | --------- |
| `controller.TestControllerIntegrationTest`  | 1     | 0      | 0       | 0         |
| `controller.UserAuthGlobalExceptionsTest`   | 2     | 0      | 0       | 0         |
| `controller.UserControllerTest`             | 1     | **1**  | 0       | 0         |
| `dto.language.LanguageDtoValidationIT`      | 14    | 0      | 0       | 0         |
| `dto.permission.PermissionDtoValidationIT`  | 8     | 0      | 0       | 0         |
| `dto.user.CreateUserDtoValidationIT`        | 18    | 0      | 0       | 0         |
| `dto.userprofile.UserProfileDtoValidationIT`| 12    | 0      | 0       | 0         |
| `dto.userstatus.UserStatusDtoValidationIT`  | 8     | 0      | 0       | 0         |
| `dto.userstatus.UserStatusTranslationDtoValidationIT` | 6 | 0      | 0       | 0         |
| `dto.usertype.UserTypeDtoValidationIT`      | 8     | 0      | 0       | 0         |
| `dto.usertype.UserTypeTranslationDtoValidationIT` | 6 | 0      | 0       | 0         |
| `i18n.MessagesI18nIntegrationTest`          | 5     | 0      | 0       | 0         |
| `mappers.LanguageMapperTest`                | 3     | 0      | 0       | 0         |
| `mappers.UserMapperTest`                    | 2     | 0      | 0       | 0         |
| `mappers.UserProfileMapperTest`             | 3     | 0      | 0       | 0         |
| `mappers.UserStatusMapperTest`              | 3     | 0      | 0       | 0         |
| `mappers.UserStatusTranslationMapperTest`   | 3     | 0      | 0       | 0         |
| `repository.LanguageRepositoryTest`         | 1     | 0      | 0       | 0         |
| `repository.UserProfileRepositoryTest`      | 2     | 0      | 0       | 0         |
| `rules.LanguageRulesTest`                   | 5     | 0      | 0       | 0         |
| `rules.UserProfileRulesTest`                | 5     | 0      | 0       | 0         |
| `rules.UserRulesTest`                       | 3     | 0      | 0       | 0         |
| `rules.UserStatusTranslationRulesTest`      | 2     | 0      | 0       | 0         |
| `service.LanguageServiceTest`               | 1     | 0      | 0       | 0         |
| `service.UserProfileServiceTest`            | 4     | 0      | 0       | 0         |
| `service.UserServiceTest`                   | 1     | 0      | 0       | 0         |
| `service.UserStatusTranslationServiceTest`  | 2     | 0      | 0       | 0         |
| `UserAuthApplicationTests`                  | 1     | 0      | 0       | 0         |
| **Total**                                   | **130** | **1**  | **0**   | **0**     |

Puntos clave del DoD confirmados en esta corrida:

- `MessagesI18nIntegrationTest` 5 de 5 en verde (los 4 métodos que
  estaban en rojo en el Ciclo 1 más `everyBusinessCodeResolvesInRealMessageSource`).
- Las 6 clases de O7 en verde con sus aserciones de `code`:
  `LanguageRulesTest` 5, `UserProfileRulesTest` 5, `UserRulesTest` 3,
  `UserStatusTranslationRulesTest` 2, `UserProfileServiceTest` 4 y
  `UserStatusTranslationServiceTest` 2.
- Los 8 `*DtoValidationIT` (80 tests), `UserAuthGlobalExceptionsTest`
  (2), los 5 mappers (14), los 2 repositories (3) y
  `UserAuthApplicationTests` (1) en verde.

### `gradlew.bat build --continue`

- `:api` — `compileJava`, `jar`, `bootJar`, `assemble`, `check` y
  `build` en verde.
- `:shared-kernel` — `compileJava`, `jar`, `assemble`, `check` y
  `build` en verde.
- `:user-auth` — `compileJava`, `jar`, `bootJar` y `assemble` en
  verde; BUILD FAILED únicamente por el fallo P-31 de `:user-auth:test`.
- Sin errores de compilación, de jar ni de assemble en ningún módulo.

## Verificación de contenido (solo lectura)

### Paridad de los dos bundle

Comparación de `shared-kernel/src/main/resources/i18n/messages.properties`
y `messages_es.properties`:

| Criterio                  | EN   | ES   | Paridad |
| ------------------------- | ---- | ---- | ------- |
| Líneas totales            | 92   | 92   | Sí      |
| Comentarios `#`           | 16   | 16   | Sí, en las mismas líneas (`1, 4, 7, 12, 19, 23, 28, 33, 38, 47, 53, 59, 64, 70, 75, 86`) |
| Líneas en blanco          | 15   | 15   | Sí, en las mismas posiciones |
| Claves                    | 61   | 61   | Sí, mismo orden línea a línea |
| Desglose de claves        | 21 de negocio + 40 de validación | igual | Sí |

- Negocio: `E-001`, `Res-001`, `S-001..003`, `Ent-001..005`,
  `Cat-001..002`, `Lan-001..003`, `Usr-001..003`, `UsrProf-001..003`
  (= 21).
- Validación: `lang` 7, `perm` 4, `usrtype` 4, `usrtypetrans` 3,
  `usrstatus` 4, `usrstatustrans` 3, `usr` 9, `usrprof` 6 (= 40).
- Cada línea de clave del EN coincide en clave y posición con su espejo
  ES; solo cambia el literal traducido.

### Cero aserciones `" context"`

- Búsqueda literal de ` context"` en `user-auth/src/test`: **1
  coincidencia, no es aserción**. Es el `@DisplayName` de
  `user-auth/src/test/java/com/icms/user_auth/i18n/MessagesI18nIntegrationTest.java:178`
  ("The 21 business codes resolve in the real MessageSource of the
  context"), que contiene la subcadena por la palabra "context", no un
  fallback `"<code> context"`.
- Ninguna llamada `.hasMessage("<código> context")` ni
  `assertEquals("<código> context", ...)` queda en el árbol de pruebas:
  las 12 aserciones de O7-A fueron retiradas/sustituidas.

### Clases de test creadas

| Clase                            | Módulo         | Métodos `@Test` | Esperado |
| -------------------------------- | -------------- | --------------- | -------- |
| `MessagesI18nIntegrationTest`    | `user-auth`    | 5 (`:93, :109, :125, :150, :177`) | 5 ✓ |
| `LanguageRulesTest`              | `user-auth`    | 5 (`:69, :85, :100, :116, :134`)  | 5 ✓ |
| `MessagesBundleStructureTest`    | `shared-kernel`| 13 (11 de lista + 2 añadidos)      | 13 ✓ |
| `MessagesBundleCoverageTest`     | `shared-kernel`| 2  | 2 ✓  |
| `MessageResolverLocaleTest`      | `shared-kernel`| 24 | 24 ✓ |

### Otras comprobaciones del plan

- `shared-kernel/.../config/i18n/MessageConfig.java:27-30` contiene el
  `@Bean messageResolver(MessageSource)` con inyección por tipo; el
  import de `MessageResolver` está en `:3`; `messageSource()` intacto.
- `pending.md:290` registra **P-31**, fuera del DoD por O6.

## Veredicto final

**PASS**

Sin incumplimientos del plan ni del Definition of Done:

1. `:shared-kernel:test` en verde con exactamente 39 tests y 0 fallos.
2. `:user-auth:test` con 129 de 130 tests en verde; el único fallo es
   el preexistente P-31, excluido del DoD por decisión O6.
3. `MessagesI18nIntegrationTest` 5 de 5 con los textos reales EN/ES.
4. `gradlew.bat build --continue` sin errores de compilación, de jar
   ni de assemble en `:api`, `:shared-kernel` y `:user-auth`.
5. Las 12 aserciones de fallback retiradas (O7 opción A) y los dos
   bundle con paridad total de 61 claves.

## Preexistente (ajeno a la refactorización)

1. `FAIL: UserControllerTest.testCreateUser, java.lang.AssertionError: 200 esperado y 400 recibido por la fila residual testuser2@example.com más el defecto de rollback del tearDown, user-auth/src/test/java/com/icms/user_auth/controller/UserControllerTest.java:72`

- Registrado en `pending.md` como **P-31** (`pending.md:290`) con su
  defecto de apoyo en el `tearDown`
  (`UserControllerTest.java:35-41`).
- Excluido del Definition of Done por el desarrollador (decisión O6):
  la línea base del proyecto, **antes de cualquier cambio de esta
  refactorización**, ya era de 120 tests con este mismo fallo.
- No imputable a esta refactorización: no toca `UserControllerTest`
  ni su BD de pruebas.

## Opcional

Sugerencias que no bloquean el veredicto ni incumplen el plan:

- Filas residuales de `languages` **ya eliminadas** el 2026-10-07 por el
  orquestador (las 4 del Ciclo 1: `ed-DP`, `yn-MO`, `cp-HC`, `kc-OO`);
  la tabla solo conserva los 3 seeds (`en-US`, `es-ES`, `fr-FR`) y la
  limpieza en `@AfterEach` de `MessagesI18nIntegrationTest` impide que se
  acumulen nuevas.
- Limpiar la fila residual `testuser2@example.com` y arreglar el
  rollback del `tearDown` de `UserControllerTest` para cerrar P-31.
- Fuerza de reejecución: en validaciones futuras, usar `--rerun` o
  `cleanTest`, porque sin él Gradle devuelve `UP-TO-DATE` y no se
  ejercitan los tests.
