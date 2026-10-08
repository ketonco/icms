# Plan: test-naming-standard

Fase 2 (re-plan): este documento reemplaza al plan anterior de este mismo slug.
El estándar de nombres cambió por decisión directa del desarrollador y todas
las preguntas abiertas quedaron cerradas; todo lo que sigue refleja el
estándar nuevo.

Slug: `test-naming-standard`.

## Objetivo

Unificar la nomenclatura de las clases de prueba del monorepo ICMS bajo un
esquema obligatorio de dos sufijos (`*Test` = unitaria, `*IT` = integración),
reescribir §I de `.github/copilot-instructions.md` para fijar ese estándar
oficial, renombrar las 9 clases que hoy no lo cumplen, actualizar todas las
referencias vivas en guías y comandos, y completar la carpeta `commands/` con
los archivos de test que faltan por módulo.

El alcance es consistencia de nombres y de documentación de comandos: no se
reescribe la suite, no se añade cobertura, no se tocan dependencias ni
migraciones.

## Cambios respecto al plan anterior

- Renombres: de 2 a 9 (ahora incluye repositorios, i18n, los dos humos de
  aplicación, `TestControllerIntegrationTest` y el test de gateway).
- Esquema de nombres: se abandonan `*IntegrationTest` y `*RepositoryTest`;
  todo lo de integración pasa a `*IT`. Se descarta además `*UT`, que nunca
  llegó a proponerse formalmente.
- `.github/copilot-instructions.md` deja de ser intocable en este alcance:
  §I:81-91 se reescribe con el nuevo estándar (autorización explícita; la
  frase "copilot-instructions JAMÁS se editan" de
  `.agents/skills/qa-department/SKILL.md:58` es el alcance de escritura de las
  skills de QA, no de esta refactorización).
- Alcance de documentación ampliado: además de `1guides/11-pruebas.md` y
  `1guides/10-gateway-tests.md`, se corrigen todas las guías afectadas y toda
  la carpeta `commands/`.
- `pending.md` y `MEMORY.md`: autorizado actualizar rutas y nombres (sin
  alterar el contenido de los hallazgos), cerrando la antigua Pregunta 3.
- `commands/gradle.md`: pasa de opcional (Pregunta 4) a incluido: se agrega la
  sección global "Ejecutar todos los tests".
- `commands/api.md`: pasa de Opción A/B (Pregunta 5) a decisión cerrada: el
  bloque `--tests` se mueve de `commands/api.md` al nuevo
  `commands/api-tests.md`, sin duplicarlo.
- Las 5 preguntas del plan anterior quedan sin efecto: no hay preguntas
  nuevas; las decisiones están cerradas.

| Aspecto | Plan anterior | Plan nuevo (este documento) |
| --- | --- | --- |
| Esquema de sufijos | 4+1: `*Test`, `*RepositoryTest`, `*IntegrationTest`, `*IT` y `*ApplicationTests` por defecto Spring | 2: `*Test` (unitaria) e `*IT` (integración) |
| Renombres | 2 | 9 |
| Inventario final | 22 `*Test` + 3 `*IntegrationTest` + 8 `*IT` | 16 `*Test` + 17 `*IT` |
| Humo `*ApplicationTests` | se mantenía | `UserAuthApplicationIT`, `ApiApplicationIT` |
| `*UT` | no evaluado | descartado por el desarrollador |
| `.github/copilot-instructions.md` | fuente de verdad intocable | §I:81-91 se reescribe (autorizado) |
| `pending.md` / `MEMORY.md` | requerían autorización | autorizado: solo rutas/nombres |
| `commands/gradle.md` | opcional (Pregunta 4) | incluido |
| `commands/api.md` | Opción A/B (Pregunta 5) | mover `--tests` sin duplicar |
| Preguntas abiertas | 5 | 0 |

## Estándar de nombres adoptado

Fuente de verdad: `.github/copilot-instructions.md:81-91`, que se reescribe
con el estándar nuevo (ver sección siguiente). Referencia secundaria:
`1guides/11-pruebas.md:10-21`.

| Sufijo | Tipo de prueba | Cuándo se usa | Ejemplos resultantes |
| --- | --- | --- | --- |
| `*Test` | Unitaria: sin contexto Spring completo ni BD real | `service/`, `rules/`, `mappers/` y tests de bundles de `shared-kernel/i18n` que leen classpath sin contexto | `LanguageServiceTest`, `UserRulesTest`, `UserMapperTest`, `MessagesBundleStructureTest` |
| `*IT` | Integración: usa contexto Spring y/o BD real | `repository/` (`@DataJpaTest`), `controller/` e `i18n/` (`RANDOM_PORT` + RestAssured), `api/userauth/` (gateway con `WebTestClient`), `dto/**` (validación con `Validator` en contexto) y humo de aplicación | `LanguageRepositoryIT`, `UserControllerIT`, `MessagesI18nIT`, `UserAuthGatewayRoutingIT`, `CreateUserDtoValidationIT`, `UserAuthApplicationIT` |

### Por qué no `*UT`

- El propio desarrollador lo descartó: la propuesta era "`*IT` y ¿`*UT`?" y la
  decisión cerrada es quedarse con `*Test` para unitarias.
- `*Test` ya es el sufijo de las 16 unitarias existentes; cambiarlas a `*UT`
  multiplicaría los renombres sin ganar información (el nombre no aporta nada
  que la tabla de la guía no diga).
- `*Test`/`*IT` es la dupla canónica del ecosistema JUnit/Gradle (Failsafe
  usa `*IT` para integración), mientras `*UT` no tiene respaldo en ninguna
  herramienta ni en el código actual.

### Por qué no `*IntegrationTest` ni `*RepositoryTest`

- El requisito explícito del desarrollador es que "todos se llamen `IT`":
  un solo sufijo para integración evita elegir entre tres variantes.
- Tres sufijos generan ambigüedad en las fronteras (i18n en contexto, DTO
  validado con `Validator` dentro de `@SpringBootTest`, humo de aplicación):
  todos son integración y todos quedan cubiertos por `*IT`.
- Ya existe la familia `*DtoValidationIT` (8 clases); con `*IT` esas 8 no se
  tocan y el codebase queda con un solo patrón de integración.
- El plan anterior habría creado `UserControllerIntegrationTest`,
  `UserAuthGlobalExceptionsIntegrationTest`, `LanguageRepositoryTest` (sin
  cambio), etc.: nombres largos y mezcla de tres sufijos. Todo eso queda
  reemplazado por la tabla de 9 renombres de este documento.

### Nota técnica: `*IT` no cambia la ejecución

- `buildSrc/src/main/kotlin/java-common-conventions.gradle.kts:52-54` solo
  hace `tasks.named<Test>("test") { useJUnitPlatform() }`: no filtra por
  nombre de clase, luego renombrar a `*IT` no deja de ejecutar nada bajo
  `gradlew test` y no crea una suite separada.
- Confirmado también en `1guides/11-pruebas.md:20-21` (la nota se reescribe
  para reflejar el estándar de dos sufijos).
- `user-auth/build.gradle:60` es un bloque `tasks.named('test', Test)`
  comentado (`/**...`), sin efecto sobre la ejecución.
- Los XML de resultados en `**/build/` se regenerarán con el FQCN nuevo;
  `build/` está ignorado por Git (`.gitignore:5`).

## Renombres exactos (9)

Cada renombre es `git mv` del archivo más la edición del texto de la
declaración de la clase en su misma línea: el número de línea de cada
declaración no cambia y queda indicado abajo como la línea exacta a editar.

### R1: `UserControllerTest` → `UserControllerIT`

- Archivo: `user-auth/src/test/java/com/icms/user_auth/controller/UserControllerTest.java:23`
  (`public class UserControllerTest {`).
- Evidencia de tipo integración: `@SpringBootTest(RANDOM_PORT)` en
  `UserControllerTest.java:21`, `@ActiveProfiles("test")` en
  `UserControllerTest.java:22`, RestAssured `given()` en
  `UserControllerTest.java:65`.
- Referencias vivas:
  - `1guides/11-pruebas.md:260` — lista de tests MVC.
  - `pending.md:71` y `pending.md:179` (ruta del archivo) y
    `pending.md:182` (nombre en prosa dentro de "Problema") — solo actualizar
    ruta/nombre, sin alterar el hallazgo P-23 (`pending.md:67`) ni P-27
    (`pending.md:176`).
  - `pending.md:293` (ruta) — hallazgo P-31 (`pending.md:290`).
  - `MEMORY.md:123` — solo el nombre.
- Sin referencias en `commands/`, `.github/`, `.kts`, `.gradle` ni `.yml`.

### R2: `UserAuthGlobalExceptionsTest` → `UserAuthGlobalExceptionsIT`

- Archivo: `user-auth/src/test/java/com/icms/user_auth/controller/UserAuthGlobalExceptionsTest.java:19`
  (`class UserAuthGlobalExceptionsTest {`).
- Evidencia de tipo integración: `@SpringBootTest(RANDOM_PORT)` en
  `UserAuthGlobalExceptionsTest.java:17`, `given()` en
  `UserAuthGlobalExceptionsTest.java:49` y `:79`.
- Referencias vivas:
  - `commands/user-auth-tests.md:19` — apunta al FQCN roto
    `com.icms.user_auth.exceptions.UserAuthGlobalExceptionsIT` (la clase no
    existe); se corrige a
    `com.icms.user_auth.controller.UserAuthGlobalExceptionsIT`. Esta línea se
    corrige haga lo que haga el resto del plan.
  - `user-auth/src/test/java/com/icms/user_auth/i18n/MessagesI18nIntegrationTest.java:28`
    — Javadoc `{@code UserAuthGlobalExceptionsTest}` → `{@code UserAuthGlobalExceptionsIT}`.
  - `1guides/11-pruebas.md:16` (ejemplo de la tabla) y
    `1guides/11-pruebas.md:260`.

### R3: `TestControllerIntegrationTest` → `TestControllerIT`

- Archivo: `user-auth/src/test/java/com/icms/user_auth/controller/TestControllerIntegrationTest.java:13`
  (`public class TestControllerIntegrationTest {`).
- Evidencia de tipo integración: `@SpringBootTest(RANDOM_PORT)` en
  `TestControllerIntegrationTest.java:11`, `@ActiveProfiles("test")` en
  `TestControllerIntegrationTest.java:12`.
- Nota: el prefijo `Test` se conserva porque nombra a la clase bajo prueba
  (`user-auth/src/main/java/com/icms/user_auth/controller/TestController.java:9`);
  solo cambia el sufijo.
- Referencias vivas:
  - `commands/user-auth-tests.md:14`.
  - `1guides/11-pruebas.md:261`.

### R4: `MessagesI18nIntegrationTest` → `MessagesI18nIT`

- Archivo: `user-auth/src/test/java/com/icms/user_auth/i18n/MessagesI18nIntegrationTest.java:36`
  (`class MessagesI18nIntegrationTest {`).
- Evidencia de tipo integración: `@SpringBootTest(RANDOM_PORT)` en
  `MessagesI18nIntegrationTest.java:34`, `given()` en
  `MessagesI18nIntegrationTest.java:95`, `:111`, `:136` y `:164`.
- Referencias vivas:
  - `1guides/11-pruebas.md:17` y `1guides/11-pruebas.md:269`.
  - `MEMORY.md:92` — solo el nombre.
  - Además, la propia clase contiene el Javadoc de R2 en
    `MessagesI18nIntegrationTest.java:28`.

### R5: `LanguageRepositoryTest` → `LanguageRepositoryIT`

- Archivo: `user-auth/src/test/java/com/icms/user_auth/repository/LanguageRepositoryTest.java:21`
  (`class LanguageRepositoryTest {`).
- Evidencia de tipo integración: `@DataJpaTest` en
  `LanguageRepositoryTest.java:17`, `@ActiveProfiles("test")` en
  `LanguageRepositoryTest.java:18`.
- Referencias vivas:
  - `commands/user-auth-tests.md:17`.
  - `1guides/11-pruebas.md:14` y `1guides/11-pruebas.md:225`.

### R6: `UserProfileRepositoryTest` → `UserProfileRepositoryIT`

- Archivo: `user-auth/src/test/java/com/icms/user_auth/repository/UserProfileRepositoryTest.java:28`
  (`public class UserProfileRepositoryTest {`).
- Evidencia de tipo integración: `@DataJpaTest` en
  `UserProfileRepositoryTest.java:24`, `@ActiveProfiles("test")` en
  `UserProfileRepositoryTest.java:25`.
- Referencias vivas: `1guides/11-pruebas.md:225`.

### R7: `UserAuthApplicationTests` → `UserAuthApplicationIT`

- Archivo: `user-auth/src/test/java/com/icms/user_auth/UserAuthApplicationTests.java:7`
  (`class UserAuthApplicationTests {`).
- Evidencia: `@SpringBootTest` en `UserAuthApplicationTests.java:6` — humo que
  arranca el `ApplicationContext` completo, es integración.
- Referencias vivas: `1guides/11-pruebas.md:291`.

### R8: `ApiApplicationTests` → `ApiApplicationIT`

- Archivo: `api/src/test/java/com/icms/api/ApiApplicationTests.java:7`
  (`class ApiApplicationTests {`).
- Evidencia: `@SpringBootTest` en `ApiApplicationTests.java:6` — humo.
- Referencias vivas: `1guides/11-pruebas.md:291`.

### R9: `UserAuthGatewayRoutingIntegrationTest` → `UserAuthGatewayRoutingIT`

- Archivo: `api/src/test/java/com/icms/api/userauth/UserAuthGatewayRoutingIntegrationTest.java:25`
  (`public class UserAuthGatewayRoutingIntegrationTest {`).
- Evidencia de tipo integración: `@SpringBootTest(RANDOM_PORT)` en
  `UserAuthGatewayRoutingIntegrationTest.java:23`, `WebTestClient` en `:30`,
  método `testGatewayRoutingToUserAuth` en `:81`.
- Referencias vivas:
  - `commands/api.md:37-38` — el bloque completo se mueve a
    `commands/api-tests.md` con el nombre nuevo (sin duplicar).
  - `1guides/10-gateway-tests.md:5` (ruta del archivo) y
    `1guides/10-gateway-tests.md:60` (comando `--tests`).

### Clases sin renombre (verificado)

Inventario completo de `**/src/test/java/**/*.java`: 33 clases. Sin renombre:

- 16 unitarias `*Test`: `service/` ×4 (`LanguageServiceTest`,
  `UserProfileServiceTest`, `UserServiceTest`,
  `UserStatusTranslationServiceTest`), `rules/` ×4 (`LanguageRulesTest`,
  `UserProfileRulesTest`, `UserRulesTest`,
  `UserStatusTranslationRulesTest`), `mappers/` ×5 (`LanguageMapperTest`,
  `UserProfileMapperTest`, `UserMapperTest`, `UserStatusMapperTest`,
  `UserStatusTranslationMapperTest`) y `shared-kernel/i18n/` ×3
  (`MessagesBundleStructureTest`, `MessagesBundleCoverageTest`,
  `MessageResolverLocaleTest`).
- 8 `*IT` ya conformes bajo `user-auth/.../dto/**`: `LanguageDtoValidationIT`,
  `CreateUserDtoValidationIT`, `PermissionDtoValidationIT`,
  `UserProfileDtoValidationIT`, `UserStatusDtoValidationIT`,
  `UserStatusTranslationDtoValidationIT`, `UserTypeDtoValidationIT`,
  `UserTypeTranslationDtoValidationIT`.
- Resultado: 16 `*Test` + 17 `*IT` = 33 clases (9 renombradas + 24 sin
  cambio).

### Barrido de referencias adicionales (verificado)

Además de las referencias listadas por el orquestador, se verificó con grep
sobre `*.md`, `*.java`, `*.kts`, `*.gradle` y `*.yml`:

- Sin coincidencias de los 9 nombres viejos en `*.kts`, `*.gradle`, `*.yml` ni
  `*.yaml`.
- Sin coincidencias en `.agents/` ni en `guia-temporal/`.
- `.github/copilot-instructions.md` no menciona ninguna de las 9 clases; solo
  los patrones de sufijo (`*RepositoryTest`, `*IntegrationTest`), que se
  corrigen en la reescritura de §I.
- `AGENTS.md` y `qa_historic.md` no mencionan ninguna de las 9 clases ni
  reglas de sufijo (grep sin coincidencias).
- Las únicas menciones fuera de alcance están en los registros históricos de
  `.opencode/refactors/` (`messages-properties-usecases/`,
  `remaining-dto-i18n/`, además de este propio plan): NO se tocan.

## Cambios en `.github/copilot-instructions.md:81-91`

Reescritura completa de §I "Convenciones de Pruebas". Redacción propuesta
(conserva intactos los aspectos técnicos de las líneas actuales 84, 85, 86,
87, 88, 89, 90 y 91; solo cambia el estándar de nombres y el criterio del
principio selectivo):

````markdown
### I. Convenciones de Pruebas

- **Sufijos de nombres (estándar de dos):** en todo el monorepo solo existen
  dos sufijos para clases de prueba. `*Test` = unitaria: no levanta el
  contexto Spring completo ni usa la BD real (servicio, reglas, mappers y
  lectura de bundles desde classpath). `*IT` = integración: usa contexto
  Spring y/o BD real (repositorio con BD de prueba, controlador y gateway con
  `RANDOM_PORT`, validación de DTO con `Validator`, i18n en contexto y humo
  de aplicación). Prohibidos `*UT`, `*IntegrationTest`, `*RepositoryTest`,
  `*ApplicationTests` y cualquier otro sufijo inventado: no crees ni
  renombres clases con ellos. Ejemplos conformes: `UserControllerIT`,
  `LanguageRepositoryIT`, `CreateUserDtoValidationIT`, `UserAuthApplicationIT`.
  El sufijo no cambia la ejecución: todo corre en la tarea `test` con
  `useJUnitPlatform()` y sin filtro por nombre
  (`buildSrc/src/main/kotlin/java-common-conventions.gradle.kts:52-54`).
- **Principio selectivo:** no re-testear comportamiento genérico heredado ya
  cubierto por otro hijo. Las clases base (`BaseRepository`, `BaseService`,
  `BaseMapper`) se validan una vez con un representativo como `Language*`.
  Solo se crea test por entidad si añade query propia, constraint, regla o
  lógica compleja. Se acepta cobertura parcial de los `*IT` de repositorio y
  `Select.field(...)` solo cuando se fijan valores.
- **Pruebas unitarias de servicio** (`src/test/.../service/*ServiceTest.java`):
  `@ExtendWith(MockitoExtension.class)`, `@Mock` para repositorio y mapper,
  `@InjectMocks` para el servicio bajo prueba. No se levanta contexto de
  Spring.
- **Pruebas unitarias de mapper** (`src/test/.../mappers/*MapperTest.java`):
  instancian el mapper vía `Mappers.getMapper(XxxMapper.class)` (sin contexto
  Spring), usando `Instancio` para generar datos aleatorios y
  `Select.field(...)` para fijar valores puntuales de verificación.
- **Pruebas de integración de repositorio** (`src/test/.../repository/*IT.java`):
  `@DataJpaTest`, `@ActiveProfiles("test")`, `@Import(AuditConfig.class)`,
  `@AutoConfigureTestDatabase(replace = Replace.NONE)` (usa la BD real de
  test, no H2), `TestEntityManager` para `persistAndFlush`. Evitar
  `Instancio.create(Entity.class)` para el ID/PK: usar el constructor de
  negocio o limpiar el `id` explícitamente antes de persistir.
- **Pruebas de integración de controlador** (`src/test/.../controller/*IT.java`):
  `@SpringBootTest(webEnvironment = RANDOM_PORT)`, `@ActiveProfiles("test")`,
  `@LocalServerPort`, peticiones HTTP reales vía `RestAssured`.
- **Pruebas de validación de DTO:** validar directamente con el `Validator`
  inyectado (`validator.validate(dto)`) en lugar de peticiones HTTP a los
  controladores, porque no todos los DTO nacen por un endpoint (ej.:
  `UserTypeController` solo implementa `ReadController`, no expone `POST`).
  Reservar los tests HTTP con RestAssured (`*IT`) para los flujos que sí
  exponen endpoint.
- **Warnings en tests nuevos:** aplicar el patrón ya usado en el proyecto —
  `@SuppressWarnings("null")` en la clase o miembro afectado para silenciar
  las advertencias de nulidad de los campos inyectados (`@Autowired`,
  `@Mock`) y dejar limpia la ventana de problemas. Nunca suprimir warnings de
  seguridad ni errores reales.
- **Pruebas del gateway** (`api/src/test/.../*IT.java`):
  `@SpringBootTest(webEnvironment = RANDOM_PORT)` con cliente `WebTestClient`
  y downstream controlado (WireMock + `@DynamicPropertySource` para
  `HOST_*`/`PORT_*`); nunca exigen el microservicio real levantado ni
  asertan sus seeds. Las pruebas con servicio vivo pertenecen a una suite de
  sistema aparte, excluida de `./gradlew test`.
- El perfil `test` (ver `application.yml`) desactiva Liquibase, sesión y
  caché para aislar las pruebas.
````

Detalle de sustitución por línea:

- `.github/copilot-instructions.md:81` — encabezado, se conserva.
- `.github/copilot-instructions.md:82` — blanco, se conserva.
- `.github/copilot-instructions.md:83` — se reemplaza por el primer bullet
  "Sufijos de nombres" más la versión nueva de "Principio selectivo" (quitan
  `*RepositoryTest` y "patrón `*IT` para IT de excepciones", que ya no es una
  excepción sino la regla única).
- `.github/copilot-instructions.md:84-85` — se conservan igual.
- `.github/copilot-instructions.md:86` — solo cambia el patrón
  `repository/*RepositoryTest.java` → `repository/*IT.java`.
- `.github/copilot-instructions.md:87` — solo cambia
  `controller/*IntegrationTest.java` → `controller/*IT.java`.
- `.github/copilot-instructions.md:88` — solo cambia `(*IntegrationTest)` →
  `(*IT)`.
- `.github/copilot-instructions.md:89` — se conserva igual.
- `.github/copilot-instructions.md:90` — solo cambia
  `api/src/test/.../*IntegrationTest.java` → `api/src/test/.../*IT.java`.
- `.github/copilot-instructions.md:91` — se conserva igual.

## Archivos de comandos

Patrón a replicar: `commands/user-auth.md:38-45` (sección corta "Ejecutar
todos los tests" con remisión al archivo `*-tests.md` correspondiente) y el
formato de `commands/user-auth-tests.md` (título `# Tests de <módulo>`,
sección de suite completa, sección "Pruebas puntuales (patrón `--tests`,
acepta comodines `*`)" y pie "Para un método puntual usa `"Clase.metodo"`;
para un paquete completo, `"paquete.*"`. No agregues una línea por clase
nueva: el patrón la cubre.").

### Crear `commands/api-tests.md`

Contenido previsto (nombres nuevos ya aplicados):

````markdown
# Tests de `api`

## Ejecutar todos los tests

```powershell
.\gradlew.bat :api:test
```

Ejecuta todos los tests del módulo `api` (unitarios `*Test` e integración
`*IT`). Los tests del gateway usan downstream controlado (WireMock) y no
requieren `user-auth` vivo; detalle en `1guides/10-gateway-tests.md`.

## Pruebas puntuales (patrón `--tests`, acepta comodines `*`)

```powershell
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIT"
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIT.testGatewayRoutingToUserAuth"
.\gradlew.bat :api:test --tests "com.icms.api.userauth.*"
.\gradlew.bat :api:test --tests "com.icms.api.ApiApplicationIT"
```

Para un método puntual usa `"Clase.metodo"`; para un paquete completo,
`"paquete.*"`. No agregues una línea por clase nueva: el patrón la cubre.
````

Verificado: el método `testGatewayRoutingToUserAuth` existe en
`api/src/test/java/com/icms/api/userauth/UserAuthGatewayRoutingIntegrationTest.java:81`.

### Crear `commands/shared-kernel-tests.md`

Contenido previsto:

````markdown
# Tests de `shared-kernel`

## Ejecutar todos los tests

```powershell
.\gradlew.bat :shared-kernel:test
```

Ejecuta todos los tests del módulo `shared-kernel` (estructura, cobertura y
Locale de los bundles i18n; todos unitarios `*Test`).

## Pruebas puntuales (patrón `--tests`, acepta comodines `*`)

```powershell
.\gradlew.bat :shared-kernel:test --tests "com.icms.shared.i18n.MessageResolverLocaleTest"
.\gradlew.bat :shared-kernel:test --tests "com.icms.shared.i18n.MessageResolverLocaleTest.resolvesEnglishForUsLocale"
.\gradlew.bat :shared-kernel:test --tests "com.icms.shared.i18n.*"
```

Para un método puntual usa `"Clase.metodo"`; para un paquete completo,
`"paquete.*"`. No agregues una línea por clase nueva: el patrón la cubre.
````

Verificado: el método `resolvesEnglishForUsLocale` existe en
`shared-kernel/src/test/java/com/icms/shared/i18n/MessageResolverLocaleTest.java:73`.

### Actualizar `commands/api.md`

- `commands/api.md:30-32`: a la explicación de "Ejecutar todos los tests" se
  le agrega la frase de remisión "Para pruebas puntuales ver `api-tests.md`.",
  replicando `commands/user-auth.md:44-45`.
- `commands/api.md:34-41`: se elimina toda la sección "Pruebas puntuales
  (patrón `--tests`...)" — su contenido pasa íntegro (y con los nombres
  nuevos) a `commands/api-tests.md`. Decisión cerrada: mover, no duplicar.

### Actualizar `commands/shared-kernel.md`

- `commands/shared-kernel.md:17` (fin de archivo): se añade, con línea en
  blanco previa, la sección:

````markdown
## Ejecutar todos los tests

```powershell
.\gradlew.bat :shared-kernel:test
```

Ejecuta todos los tests del módulo `shared-kernel`. Para pruebas puntuales
ver `shared-kernel-tests.md`.
````

### Actualizar `commands/user-auth-tests.md`

Bloque `commands/user-auth-tests.md:13-20` resultante (línea 14 renombrada,
línea 17 renombrada, línea 19 corregida, y 3 líneas nuevas agregadas al
final):

```powershell
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.controller.TestControllerIT"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.mappers.LanguageMapperTest.createEntityFromDto"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.service.*"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.repository.LanguageRepositoryIT"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.rules.UserStatusTranslationRulesTest"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.controller.UserAuthGlobalExceptionsIT"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.dto.*"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.i18n.MessagesI18nIT"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.UserAuthApplicationIT"
```

Cambios puntuales:

- `commands/user-auth-tests.md:14`: `TestControllerIntegrationTest` →
  `TestControllerIT`.
- `commands/user-auth-tests.md:17`: `LanguageRepositoryTest` →
  `LanguageRepositoryIT`.
- `commands/user-auth-tests.md:19`: FQCN roto
  `com.icms.user_auth.exceptions.UserAuthGlobalExceptionsIT` →
  `com.icms.user_auth.controller.UserAuthGlobalExceptionsIT`.
- Se añaden las 3 líneas que faltaban (`dto.*`, `i18n.MessagesI18nIT` y
  `UserAuthApplicationIT`).
- `commands/user-auth-tests.md:15` y `:18` quedan igual; el método
  `createEntityFromDto` referenciado en la línea 15 existe en
  `LanguageMapperTest.java:49`.

### Actualizar `commands/gradle.md`

- `commands/gradle.md:57` (fin de archivo): se añade, con línea en blanco
  previa, la sección global:

````markdown
## Ejecutar todos los tests

```powershell
.\gradlew.bat test
```

Ejecuta la suite completa del monorepo (`shared-kernel`, `user-auth` y
`api`), con unitarias `*Test` e integración `*IT`. Para pruebas puntuales por
módulo ver `user-auth-tests.md`, `api-tests.md` y `shared-kernel-tests.md`.
````

## Guías `1guides/`

### `1guides/11-pruebas.md`

- `1guides/11-pruebas.md:10-21`: a la tabla "Qué se verifica → Tipo →
  Ejemplo actual" se le agrega una columna `Sufijo` y se actualizan los
  ejemplos renombrados. Resultado previsto:

  | Qué se verifica | Tipo | Sufijo | Ejemplo actual |
  | --- | --- | --- | --- |
  | Una regla o transformación Java sin Spring ni BD | Unitaria | `*Test` | `UserRulesTest`, `LanguageMapperTest` |
  | Servicio coordinando mocks | Unitaria de servicio | `*Test` | `UserProfileServiceTest` |
  | Query real, persistencia o constraint | Integración de repositorio | `*IT` | `LanguageRepositoryIT` |
  | DTO y sus restricciones | Validación directa | `*IT` | `CreateUserDtoValidationIT` |
  | HTTP, filtros, serialización y errores | Integración MVC | `*IT` | `UserAuthGlobalExceptionsIT` |
  | Resolución real de mensajes dentro del contexto Spring | Integración i18n | `*IT` | `MessagesI18nIT` |
  | Enrutamiento reactivo del Gateway | Integración WebFlux con downstream simulado | `*IT` | `UserAuthGatewayRoutingIT` |

- `1guides/11-pruebas.md:20-21`: corregir la nota para que refleje el
  estándar de dos sufijos y la verificación técnica:

  > El sufijo no cambia la ejecución: ni `*Test` ni `*IT` crean una suite
  > separada. Todas las clases viven bajo `src/test` y corren en la tarea
  > `test` con `useJUnitPlatform()`, sin filtro por nombre
  > (`buildSrc/src/main/kotlin/java-common-conventions.gradle.kts:52-54`).

- `1guides/11-pruebas.md:14`: `LanguageRepositoryTest` → `LanguageRepositoryIT`.
- `1guides/11-pruebas.md:16`: `UserAuthGlobalExceptionsTest` →
  `UserAuthGlobalExceptionsIT`.
- `1guides/11-pruebas.md:17`: `MessagesI18nIntegrationTest` → `MessagesI18nIT`.
- `1guides/11-pruebas.md:18`: `UserAuthGatewayRoutingIntegrationTest` →
  `UserAuthGatewayRoutingIT`.
- `1guides/11-pruebas.md:225`: `LanguageRepositoryTest` y
  `UserProfileRepositoryTest` → `LanguageRepositoryIT` y
  `UserProfileRepositoryIT`.
- `1guides/11-pruebas.md:260-261`: `UserControllerTest`,
  `UserAuthGlobalExceptionsTest` y `TestControllerIntegrationTest` →
  `UserControllerIT`, `UserAuthGlobalExceptionsIT` y `TestControllerIT`.
- `1guides/11-pruebas.md:269`: `MessagesI18nIntegrationTest` → `MessagesI18nIT`.
- `1guides/11-pruebas.md:291`: `ApiApplicationTests` y
  `UserAuthApplicationTests` → `ApiApplicationIT` y `UserAuthApplicationIT`.
- Sin cambio: `1guides/11-pruebas.md:12-13` (ejemplos unitarios ya conformes),
  `:242` (`*DtoValidationIT`), `:306` (`MessageResolverLocaleTest`).

### `1guides/10-gateway-tests.md`

- `1guides/10-gateway-tests.md:5`: ruta
  `.../UserAuthGatewayRoutingIntegrationTest.java` →
  `.../UserAuthGatewayRoutingIT.java`.
- `1guides/10-gateway-tests.md:60`: comando
  `--tests "com.icms.api.userauth.UserAuthGatewayRoutingIntegrationTest"` →
  `--tests "com.icms.api.userauth.UserAuthGatewayRoutingIT"`.

## `pending.md` y `MEMORY.md` (solo rutas y nombres)

Actualización mecánica; el contenido de los hallazgos no cambia.

- `pending.md:71`: ruta `controller/UserControllerTest.java:71` →
  `controller/UserControllerIT.java:71`.
- `pending.md:179`: ruta `controller/UserControllerTest.java:76` →
  `controller/UserControllerIT.java:76`.
- `pending.md:182`: nombre en prosa `UserControllerTest` → `UserControllerIT`.
- `pending.md:293`: ruta `controller/UserControllerTest.java:35-44` →
  `controller/UserControllerIT.java:35-44`.
- `MEMORY.md:92`: `MessagesI18nIntegrationTest` → `MessagesI18nIT`.
- `MEMORY.md:123`: `UserControllerTest` → `UserControllerIT`.
- Las líneas `pending.md:67` (P-23), `:176` (P-27) y `:290` (P-31) no se
  tocan: son encabezados sin nombres de clase.

## Qué NO se toca (principio selectivo)

- Las 24 clases ya conformes (16 `*Test` + 8 `*DtoValidationIT`): ningún
  renombre ni edición.
- Los registros históricos `.opencode/refactors/**`: `messages-properties-usecases/`,
  `usertype-dto-i18n/`, `user-status-dto-i18n/`, `remaining-dto-i18n/`.
  Única excepción: este mismo `plan.md`.
- No se mueve `commands/` a `.opencode/commands/` (decisión del desarrollador:
  los comandos por módulo viven en `commands/` en la raíz).
- No se crean, borran ni reescriben clases de test; no se añade cobertura.
- No se tocan `gradle/libs.versions.toml`, ningún `build.gradle` ni
  `buildSrc/` (sin filtro por nombre: `java-common-conventions.gradle.kts:52-54`;
  `user-auth/build.gradle:60` es un bloque comentado sin efecto).
- No se tocan migraciones Liquibase ni esquema de BD.
- No se corrigen los fallos preexistentes de los tests (el rojo histórico
  `UserControllerTest.testCreateUser`, registrado en P-23/P-27/P-31 de
  `pending.md:67`, `pending.md:176` y `pending.md:290`, sigue fuera de
  alcance: tras el renombre pasa a llamarse
  `UserControllerIT.testCreateUser`).
- `AGENTS.md`: no menciona sufijos ni clases de test; queda intacto.

## Riesgos y mitigación

- Referencias colgantes tras los 9 renombres → barrido final con grep de los
  9 nombres viejos en `*.md`, `*.java`, `*.kts`, `*.gradle` y `*.yml`:
  cero coincidencias fuera de `.opencode/refactors/**` (los registros
  históricos de ahí dentro no se tocan).
- Comandos `--tests` con FQCN obsoleto → todos los ejemplos de los tres
  `*-tests.md` se ejecutan en la validación; Gradle falla con "No tests found
  for given includes" si alguno quedó roto.
- Confundir un fallo nuevo con uno preexistido → capturar la línea base del
  suite completo (`.\gradlew.bat test`) antes de renombrar y comparar
  conteos: el único rojo admitido es el ya registrado en `pending.md` (P-31).
- Historial de Git → los renombres se hacen con `git mv` (el commit lo hace
  el desarrollador; el executor no commitea).
- Desalineación entre §I, la guía y los comandos → el orden de ejecución es:
  renombrar clases, luego §I, luego guías y comandos, para que ningún
  documento quede apuntando a nombres viejos.
- markdownlint en los `.md` nuevos/editados → verificar 0 warnings (líneas en
  blanco alrededor de encabezados, listas, tablas y bloques de código; sin
  saltos de nivel; tablas con `| --- |`).
- Los XML en `**/build/test-results/` cambian de FQCN: es artefacto ignorado
  por Git (`.gitignore:5`), sin impacto versionado.

## Tests de validación (Fase 3)

1. Línea base previa a cualquier cambio: `.\gradlew.bat test` y registrar
   conteo de tests/fallos.
2. Tras los renombres: `.\gradlew.bat :user-auth:test` → mismo conteo que la
   línea base; las 7 clases renombradas de `user-auth`
   (`controller.UserControllerIT`, `controller.UserAuthGlobalExceptionsIT`,
   `controller.TestControllerIT`, `i18n.MessagesI18nIT`,
   `repository.LanguageRepositoryIT`, `repository.UserProfileRepositoryIT` y
   `UserAuthApplicationIT`) descubiertas y en verde/rojo según línea base.
3. `.\gradlew.bat :api:test` → `api.ApiApplicationIT` y
   `api.userauth.UserAuthGatewayRoutingIT` descubiertos; mismo conteo que la
   línea base.
4. `.\gradlew.bat :shared-kernel:test` → sin cambios respecto a la línea base
   (módulo no renombrado).
5. Ejecutar cada línea `--tests` de `commands/user-auth-tests.md`,
   `commands/api-tests.md` y `commands/shared-kernel-tests.md` → todas casan
   con al menos una clase.
6. Conteo final de inventario: 16 archivos `*Test.java` y 17 archivos
   `*IT.java` bajo `**/src/test/java/**` (33 en total).
7. Grep de los 9 nombres viejos sobre `*.md`, `*.java`, `*.kts`, `*.gradle` y
   `*.yml` → cero coincidencias fuera de `.opencode/refactors/**`.
8. markdownlint sobre todos los `.md` nuevos/editados
   (`.github/copilot-instructions.md`, `1guides/11-pruebas.md`,
   `1guides/10-gateway-tests.md`, `commands/api.md`,
   `commands/api-tests.md`, `commands/shared-kernel.md`,
   `commands/shared-kernel-tests.md`, `commands/user-auth-tests.md`,
   `commands/gradle.md`, `pending.md`, `MEMORY.md`) → 0 warnings.

## Orden de ejecución sugerido (Fase 3)

1. Línea base de `.\gradlew.bat test`.
2. `git mv` de los 9 archivos y edición de las 9 declaraciones de clase (más
   el Javadoc de `MessagesI18nIntegrationTest.java:28`).
3. Reescritura de §I en `.github/copilot-instructions.md:81-91`.
4. Actualización de `1guides/11-pruebas.md` y `1guides/10-gateway-tests.md`.
5. Creación de `commands/api-tests.md` y `commands/shared-kernel-tests.md`;
   actualización de `commands/api.md`, `commands/shared-kernel.md`,
   `commands/user-auth-tests.md` y `commands/gradle.md`.
6. Actualización de rutas/nombres en `pending.md` y `MEMORY.md`.
7. Barrido de grep, suites y líneas `--tests`; markdownlint.
