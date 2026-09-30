# PENDING.md — Pendientes activos del proyecto ICMS

Validación de `.github/copilot-instructions.md` vs código real. Fecha: 2026-09-30.
Solo lectura y marcadores `TODO`; nada de lo listado está corregido todavía.

## P-01 — `starter-webflux` en tests de `user-auth` viola la regla A de aislamiento

**Dónde:** `user-auth/build.gradle:24`

**Ubicacion del TODO: agregado** `user-auth/build.gradle:24`

**Problema:** la regla A de `.github/copilot-instructions.md:19` prohíbe
dependencias reactivas en módulos MVC, pero el módulo declara
`testImplementation libs.spring.boot.starter.webflux`.

**Contexto / Explicación:** el código de `src/main` está limpio (sin
`Mono`/`Flux`); la dependencia solo afecta al classpath de tests y
normalmente se trae por `WebTestClient` (cliente reactivo de pruebas).
La regla queda en mentira mientras no se documente la excepción ni se migre.

**Opciones estándar:**

- A) Mantenerla y documentar la excepción en las instrucciones (“prohibido en
  `src/main`; permitido `WebTestClient` solo en `src/test`”).
- B) Eliminarla y usar `spring-boot-starter-webmvc-test` + `rest-assured-mockmvc`
  (ya declarados en `build.gradle:20,26`), que cubren tests MVC sin Reactor.

**Recomendación:** B si ningún test usa `WebTestClient`; si alguno lo usa, A.

**Nivel de acción requerido:** Medio — solo afecta al classpath de tests, pero
requiere decisión antes de cerrar la regla A.

## P-02 — Secuencias huérfanas en 5 migraciones Liquibase

**Dónde:** `user-auth/src/main/resources/db/migrations/20260912_0001_create_userstatus_001.yaml:7-18`
(y equivalentes en `20260915_0001_create_usertype_001.yaml`,
`20260916_0001_create_permission.yaml`,
`20260914_0001_create_userstatus_translation_001.yaml`,
`20260915_0002_create_usertype_translation_002.yaml`)

**Ubicacion del TODO: agregado** en la columna `id` de cada uno de los 5
archivos (comentario `# TODO (pending P-02)`)

**Problema:** las 5 tablas crean una secuencia explícita con `createSequence`
pero la columna `id` usa `autoIncrement: true`, que genera su propia secuencia
implícita. Las 5 secuencias manuales nunca se consumen: son peso muerto y rompen
la convención de la sección J. Referencia correcta:
`20260909_0001_create_languages_001.yaml:7-19` (`createSequence` +
`defaultValueSequenceNext`).

**Contexto / Explicación:** en Postgres ambas formas de autogenerar el `id`
son excluyentes por columna: `autoIncrement` crea columna `SERIAL`/`IDENTITY`
con secuencia implícita; `defaultValueSequenceNext` consume la secuencia
explícita. Hibernate (`GenerationType.AUTO`) funciona igual con la implícita,
por eso el fallo es silencioso. `users` (UUID con `gen_random_uuid()`) está
fuera de esta regla.

**Opciones estándar:**

- A) Alinear a la convención (recomendado si la BD aún no está en producción):
  nuevo changeset por tabla que elimine `autoIncrement` y ponga
  `defaultValueSequenceNext` con su secuencia. Nunca editar un changeset ya
  ejecutado.
- B) Si la BD ya está en producción: dejar los `id` como están y en un
  changeset nuevo eliminar las 5 secuencias huérfanas (`dropSequence`).

**Recomendación:** A si la BD es solo local; B si ya corrió en compartida.
Incluye mini-manual: ver explicación completa arriba.

**Nivel de acción requerido:** Medio — sin impacto funcional hoy, pero genera
deuda de esquema y rompe la convención J.

## P-03 — Versiones hardcodeadas fuera de `libs.versions.toml`

**Dónde:** `shared-kernel/build.gradle:12` y
`buildSrc/src/main/kotlin/migration-conventions.gradle.kts:16`

**Ubicacion del TODO: no agregado** (aparcado, lo revisa el desarrollador)

**Problema:** `shared-kernel/build.gradle:12` fija `mavenBom '...:4.1.1'` y
`migration-conventions.gradle.kts:16` fija
`liquibaseRuntime("info.picocli:picocli:4.7.6")` en vez de usar el catálogo
`libs`, lo que contradice el “estrictamente `libs.versions.toml`” de §1.

**Contexto / Explicación:** las versiones dispersas fuera del catálogo
centralizado se desincronizan con el tiempo y rompen la regla de §1.

**Opciones estándar:**

- A) Migrar ambas a `libs` (recomendado).
- B) Documentar la excepción en §1 si hay motivo técnico.

**Recomendación:** A, pendiente de revisión del desarrollador.

**Nivel de acción requerido:** Bajo — consistencia de build, sin impacto
funcional.

## P-04 — §1/B inexactos: convenciones web, typo y R2DBC

**Dónde:** `.github/copilot-instructions.md:10` (§1), `:24` (typo) y `:24`
(sección B, R2DBC)

**Ubicacion del TODO: no agregado** (solo documentación)

**Problema:** tres inexactitudes: (1) §1 dice que los backend “heredan aplicando
los plugins correspondientes”, pero `user-auth/build.gradle:1-4` solo aplica
`spring-jpa-conventions` + `migration-conventions` y declara `starter-webmvc`
directo en `:17`; (2) typo “chamadas” (portugués) en `:24`; (3) la sección B
exige “R2DBC o conectores no bloqueantes”, pero no existe ni una dependencia ni
una clase R2DBC en el repo — `api` hoy no tiene persistencia propia, solo
enruta (`spring.cloud.gateway.server.webflux`).

**Contexto / Explicación:** el plugin `spring-web-conventions` existe y aporta
`starter-web`, pero `user-auth` no lo usa; no es error funcional. La exigencia
R2DBC es un estándar futuro que hoy nada cumple.

**Opciones estándar:**

- A) Aplicar `spring-web-conventions` en `user-auth` y quitar el
  `starter-webmvc` directo; suavizar R2DBC a “el gateway no tiene persistencia;
  los downstream se consumen por HTTP no bloqueante; si un módulo reactivo
  necesitara BD, usar R2DBC”.
- B) Documentar `webmvc` directo a propósito y dejar R2DBC como estándar futuro
  (aceptando que hoy nada la cumple).

**Recomendación:** A para R2DBC (texto honesto); convenciones a decisión del
desarrollador.

**Nivel de acción requerido:** Bajo — solo documentación.

## P-05 — E inexacto: `MapperConfig` no existe, es `MapperSetting`

**Dónde:** `.github/copilot-instructions.md:46`

**Ubicacion del TODO: no agregado** (solo documentación)

**Problema:** el texto dice “interfaz `@MapperConfig` centralizada”, pero la
clase real es `shared-kernel/.../config/mapper/MapperSetting.java:7-10`
(`componentModel="spring"`, `unmappedTargetPolicy=IGNORE`,
`nullValuePropertyMappingStrategy=IGNORE`). Además `BaseMapper.java:13` expone
`updateEntityFromDto(D, E)` con `@MappingTarget`, que la instrucción no menciona.

**Contexto / Explicación:** si Copilot busca `MapperConfig` no la encuentra y
puede generar una clase duplicada.

**Opciones estándar:**

- A) Renombrar el texto a “`MapperSetting` centralizada (`spring`, `IGNORE`,
  `nullValuePropertyMappingStrategy=IGNORE`)” y añadir `updateEntityFromDto`
  al contrato de `BaseMapper` (recomendado).
- B) Renombrar la clase Java a `MapperConfig` (innecesario y más invasivo).

**Recomendación:** A.

**Nivel de acción requerido:** Bajo — solo documentación, pero evita duplicados
generados por IA.

## P-06 — Reglas de traducción: conteo mínimo, `!=` vs `equals` y mensaje descartado

**Dónde:** `shared-kernel/src/main/java/com/icms/shared/rules/BaseDaoCatalogTranslationRules.java:35,46`,
`shared-kernel/src/main/resources/i18n/messages.properties:28`,
`shared-kernel/src/main/java/com/icms/shared/exceptions/EntityNotFoundException.java:8-10`

**Ubicacion del TODO: no agregado** (a revisar por el desarrollador)

**Problema:** tres inconsistencias: (1) el texto dice “exige mínimo dos
traducciones antes de permitir un borrado”, pero el código (`:46`,
`if (count < 2)`) bloquea solo si queda menos de 1 restante, y `Lan-006` dice
“at least **one** translation”; (2) `:35` compara IDs con `!=` en vez de
`equals` (falso negativo con `Long` fuera del rango cacheado; la clase vecina
de catálogo ya usa `equals`); (3) `EntityNotFoundException` fuerza
`super("Ent-001", message)` y `BaseException.getMessage()` re-resuelve por
`code`, descartando el mensaje custom.

**Contexto / Explicación:** el `!=` entre objetos `Long` compara referencias y
puede permitir duplicados catalog+idioma; el criterio de borrado debe
unificarse en un solo número (todo apunta a “no dejar un catálogo con cero
traducciones” → “mínimo una”).

**Opciones estándar:**

- A) Unificar a “mínimo una” (código + `Lan-006` + doc), cambiar `!=` por
  `equals` y decidir si `EntityNotFoundException` conserva el mensaje custom
  (recomendado).
- B) Unificar a “mínimo dos” real (`count <= 2`) si esa era la intención
  original.

**Recomendación:** A, a validar por el desarrollador.

**Nivel de acción requerido:** Alto — el `!=` es riesgo de corrección real
(duplicados permitidos).

## P-07 — `GlobalExceptionHandler` usa código `"000"` sin i18n

**Dónde:** `shared-kernel/src/main/java/com/icms/shared/config/exception/GlobalExceptionHandler.java:19-27,41`

**Ubicacion del TODO: agregado** en `:20` (handler `NoResourceFound`) y `:42`
(handler genérico `Exception`)

**Problema:** ambos handlers devuelven código de negocio `"000"` literal y el
primero un mensaje hardcodeado en español (`"URL no encontrada: ..."`), fuera
del sistema i18n (`MessageResolver` + `messages*.properties`) que ya usan
`S-000/S-001/S-002` y los handlers de `EntityNotFound`/`BusinessRule`
(`ex.getCode()`).

**Contexto / Explicación:** respuestas de error inconsistentes: unas con código
de negocio trazable y mensaje multi-idioma, otras no.

**Opciones estándar:**

- A) Crear códigos de negocio (p. ej. `S-404` recurso no encontrado, `S-500`
  error inesperado) en `i18n/messages*.properties` y resolverlos con
  `MessageResolver` (recomendado).
- B) Dejar `"000"` como código genérico documentado.

**Recomendación:** A.

**Nivel de acción requerido:** Medio — consistencia de API y observabilidad,
sin riesgo funcional.

## P-08 — Sección I de tests no refleja la realidad

**Dónde:** `.github/copilot-instructions.md:68-73` vs `user-auth/src/test`

**Ubicacion del TODO: no agregado** (solo documentación)

**Problema:** cuatro desvíos: (1) `LanguageServiceTest.java:10,22`,
`UserStatusTranslationServiceTest.java:22` y `UserProfileServiceTest.java:8`
llevan `@Profile("test")`, no documentado; (2) `Select.field(...)` solo lo usan
`LanguageMapperTest` y `UserMapperTest`, el resto usa `Instancio.create`
directo; (3) solo existe 1 `*RepositoryTest` (`LanguageRepositoryTest`);
(4) la IT de excepciones vive en
`exceptions/UserAuthGlobalExceptionsIT.java` (patrón `*IT`), no en
`controller/*IntegrationTest`.

**Contexto / Explicación:** `@Profile` en tests Mockito puros (sin contexto
Spring) no hace nada; es inofensivo pero confunde. El resto son cobertura
parcial y convención de nombres no aplicada.

**Opciones estándar:**

- A) Quitar `@Profile` de unitarios (o documentarlo como “marca sin efecto”),
  relajar `Select.field` a “cuando se fijen valores”, documentar cobertura
  parcial de repositorios y aceptar el patrón `*IT` (recomendado).
- B) Crear los `*RepositoryTest` faltantes y mover la IT a `controller/`.

**Recomendación:** A ahora; B como trabajo futuro.

**Nivel de acción requerido:** Bajo — solo documentación (salvo que se elija B).

## P-09 — Nombres de archivo y `id` de changeset fuera de convención J

**Dónde:** `user-auth/src/main/resources/db/migrations/20260910_0001_alter_lenguages_sequence_increment.yaml`
(typo `lenguages`), `20260911_0001_alter_languages_code_length.yaml`,
`20260916_0001_create_permission.yaml`, `20260916_0002_create_user.yaml`,
`20260916_0003_create_user_profile.yaml`,
`20260916_0004_create_user_types.yaml`; `.github/copilot-instructions.md:77-78`

**Ubicacion del TODO: no agregado** (renombrar requiere decisión; ver contexto)

**Problema:** 6 archivos sin sufijo `_NNN` y la regla “`id` = filename sin
extensión” solo vale para el primer changeset de archivos conformes (el resto
añade sufijos `_indexes_002`, `_revision_003`). Casos: `20260911...` tiene un
`id` totalmente distinto al archivo; `20260916_0001...` usa plural
(`..._create_permissions_001`) vs singular del archivo.

**Contexto / Explicación:** los archivos no se pueden renombrar sin romper
checksums de BD ya migradas; si la BD es solo local, renombrar es seguro.

**Opciones estándar:**

- A) Si la BD es solo local: renombrar a formato y corregir el typo
  `lenguages` → `languages` (recomendado).
- B) Si ya corrió en compartida: documentar la excepción y aplicar el formato
  solo a archivos nuevos.

**Recomendación:** A o B según entorno, a revisar por el desarrollador.

**Nivel de acción requerido:** Bajo — convención y decisión de entorno.

## Nota — Omitido a petición del desarrollador

- Java 24 (doc) vs Java 25 (entorno): Gradle falla con
  `IllegalArgumentException: 25` en `JavaVersion.parse`. Se deja así por ahora.
