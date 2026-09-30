# Pendientes ICMS — validación de `copilot-instructions.md` vs código real

> Solo lectura y `TODO`s. Nada de lo listado aquí está corregido todavía,
> salvo los marcadores `TODO (pending P-XX)` que ya existen en el código.
> Fecha: 2026-09-30.

## P-01 — `starter-webflux` en tests de `user-auth` (TODO en código)

**Dónde:** `user-auth/build.gradle:24` (`testImplementation libs.spring.boot.starter.webflux`).

**Problema:** la regla A de `.github/copilot-instructions.md:19` prohíbe
dependencias reactivas en módulos MVC, pero este módulo la declara en tests.

**Contexto:** el código `src/main` está limpio (sin `Mono`/`Flux`); la
dependencia solo afecta al classpath de tests. Normalmente se trae por
`WebTestClient` (cliente reactivo de pruebas).

**Opciones estándar:**

- A) Mantenerla y documentar la excepción en las instrucciones (“prohibido en
  `src/main`; permitido `WebTestClient` solo en `src/test`”).
- B) Eliminarla y usar `spring-boot-starter-webmvc-test` + `rest-assured-mockmvc`
  (ya declarados en `build.gradle:20,26`), que cubren tests MVC sin Reactor.
- C) Mantenerla sin documentar (no recomendado: la regla queda en mentira).

**Recomendación:** B si ningún test usa `WebTestClient`; si alguno lo usa, A.

## P-02 — Mini-manual Liquibase: secuencias huérfanas (TODO en 5 YAML)

**Dónde (TODO ya agregado):**

- `user-auth/.../migrations/20260912_0001_create_userstatus_001.yaml` (`userstatus_seq`)
- `user-auth/.../migrations/20260915_0001_create_usertype_001.yaml` (`usertypes_seq`)
- `user-auth/.../migrations/20260916_0001_create_permission.yaml` (`permissions_seq`)
- `user-auth/.../migrations/20260914_0001_create_userstatus_translation_001.yaml` (`userstatus_translation_seq`)
- `user-auth/.../migrations/20260915_0002_create_usertype_translation_002.yaml` (`usertype_translation_seq`)

**Referencia correcta:** `20260909_0001_create_languages_001.yaml:7-19`
(`createSequence languages_seq` + columna `id` con
`defaultValueSequenceNext: languages_seq`).

**Explicación:** en Postgres hay dos formas de autogenerar el `id`, y son
excluyentes por columna:

1. `autoIncrement: true` → Liquibase crea una columna `SERIAL`/`IDENTITY`
   con su propia secuencia implícita. La secuencia creada a mano con
   `createSequence` **nunca se usa**: queda huérfana.
2. `defaultValueSequenceNext: <seq>` → la columna toma el valor de la
   secuencia explícita creada con `createSequence`. Es lo que hace `languages`
   y `revinfo`, y lo que la instrucción J exige.

Las 5 tablas listadas mezclan ambas: crean secuencia explícita pero la columna
usa `autoIncrement`. Hibernate (`GenerationType.AUTO`) funciona igual porque
usa la secuencia implícita, pero las 5 secuencias manuales son peso muerto y la
convención del proyecto queda rota.

**Opciones estándar:**

- A) Alinear a la convención (recomendado para tablas nuevas y si la BD aún no
  está en producción): nuevo changeset por tabla que elimine `autoIncrement` y
  ponga `defaultValueSequenceNext` con su secuencia. Nunca editar un changeset
  ya ejecutado.
- B) Si la BD ya está en producción: dejar los `id` como están y en un
  changeset nuevo eliminar las 5 secuencias huérfanas (`dropSequence`).
- C) Cambiar la convención a `autoIncrement` y no crear secuencias manuales
  (más simple, pero hay que reescribir la sección J y el ejemplo de
  `languages`/`revinfo`).

**Nota:** `users` usa `UUID` con `gen_random_uuid()` y está fuera de esta regla.

## P-03 — Versiones hardcodeadas (APARCADO, sin TODO, lo revisas tú)

- `shared-kernel/build.gradle:12` fija `mavenBom '...:4.1.1'` en vez de `libs`.
- `buildSrc/.../migration-conventions.gradle.kts:16` fija
  `liquibaseRuntime("info.picocli:picocli:4.7.6")` en vez de `libs.picocli`.

Contradicen el “estrictamente `libs.versions.toml`” de §1. Pendiente tu decisión.

## P-04 — §1/B: `spring-web-conventions`, typo y R2DBC (explicación)

**Qué encontré:**

- `user-auth/build.gradle:1-4` solo aplica `spring-jpa-conventions` +
  `migration-conventions`, y declara `starter-webmvc` directo en `:17`. El
  plugin `spring-web-conventions` existe y aporta `starter-web`, pero el módulo
  no lo usa. No es error funcional, pero la instrucción §1 (`:10`) dice que los
  backend “heredan aplicando los plugins correspondientes”, lo cual hoy no se
  cumple al pie de la letra.
- Typo en `.github/copilot-instructions.md:24`: “chamadas” (portugués) →
  “llamadas”.
- R2DBC: la sección B exige “R2DBC o conectores no bloqueantes”, pero en todo
  el repo no hay ni una dependencia ni una clase R2DBC; `api` (gateway) hoy **no
  tiene persistencia propia**, solo enruta (`spring.cloud.gateway.server.webflux`).

**Decisiones a tomar:**

- Convenciones: A) aplicar `spring-web-conventions` en `user-auth` y quitar el
  `starter-webmvc` directo; B) documentar que `user-auth` usa `webmvc` directo
  a propósito (p. ej. por el starter de tests `webmvc-test`).
- R2DBC: A) suavizar el texto a “el gateway no tiene persistencia; los
  downstream se consumen por HTTP no bloqueante; si un módulo reactivo necesitara
  BD, usar R2DBC”; B) dejar la exigencia como estándar futuro (aceptando que hoy
  nada la cumple).

## P-05 — E: `MapperConfig` vs `MapperSetting` (ajuste propuesto y por qué)

**Real:** `shared-kernel/.../config/mapper/MapperSetting.java:7-10`
(`componentModel="spring"`, `unmappedTargetPolicy=IGNORE`,
`nullValuePropertyMappingStrategy=IGNORE`). Además `BaseMapper.java:13` tiene
`updateEntityFromDto(D, E)` con `@MappingTarget`, que la instrucción no menciona.

**Ajuste propuesto:** cambiar `copilot-instructions.md:46` de “interfaz
`@MapperConfig` centralizada” a “`MapperSetting` centralizada (`spring`,
`IGNORE`, `nullValuePropertyMappingStrategy=IGNORE`)” y añadir
`updateEntityFromDto` al contrato de `BaseMapper`.

**Por qué:** la instrucción debe nombrar la clase real; si Copilot busca
`MapperConfig` no la encuentra y genera una nueva duplicada. Sin TODO en código
(es solo doc).

## P-06 — F: reglas de traducción (A REVISAR POR TI, sin TODO)

- `BaseDaoCatalogTranslationRules.java:46`: `if (count < 2)` bloquea el borrado
  solo si queda **menos de 1** traducción restante; el texto dice “exige mínimo
  dos antes de permitir un borrado” y `messages.properties:28` (`Lan-006`) dice
  “at least **one** translation”. Hay que unificar un solo criterio (todo apunta
  a que la intención es “no dejar un catálogo con cero
  traducciones” → redactar como “mínimo una”).
- `:35`: compara IDs con `!=` en vez de `equals` (falso negativo con `Long`
  cacheados fuera de rango); la clase vecina de catálogo ya usa `equals`.
- `EntityNotFoundException.java:8-10` + `BaseException.java:23-24`: el mensaje
  custom se descarta (se re-resuelve por `code`). Decidir si se conserva.

## P-07 — H: códigos `"000"` sin i18n (TODO en código)

**Dónde (TODO ya agregado):** `GlobalExceptionHandler.java:19-27`
(`NoResourceFound`, mensaje hardcodeado en español + `"000"`) y `:41`
(handler genérico, `"000"`).

**Recomendación:** crear códigos de negocio (p. ej. `S-404` recurso no
encontrado, `S-500` error inesperado) en `i18n/messages*.properties` y
resolverlos con `MessageResolver`, como ya hacen `S-000/S-001/S-002` y los
handlers de `EntityNotFound/BusinessRule` (`ex.getCode()`).

## P-08 — I: tests, explicación (sin TODO, solo doc)

- `@Profile("test")` en `LanguageServiceTest.java:10,22`,
  `UserStatusTranslationServiceTest.java:22`, `UserProfileServiceTest.java:8`:
  en tests Mockito puros (sin contexto Spring) la anotación no hace nada; es
  inofensiva pero confunde. Opciones: quitarla, o documentarla (“marca de
  clasificación, sin efecto en unitarios”).
- `Select.field(...)` no es universal: solo `LanguageMapperTest` y
  `UserMapperTest` lo usan; `UserStatusMapperTest`,
  `UserStatusTranslationMapperTest` y `UserProfileMapperTest` usan
  `Instancio.create` directo. Relajar el texto a “usar `Select.field` cuando se
  fijen valores de verificación”.
- Solo existe 1 `*RepositoryTest` (`LanguageRepositoryTest`); no hay tests de
  los demás repositorios. O se documenta como cobertura parcial o se crean.
- La IT de excepciones vive en `exceptions/UserAuthGlobalExceptionsIT.java`
  (nombre `*IT`), no en `controller/*IntegrationTest`. Ajustar la ruta/patrón
  en el texto o mover el archivo.

## P-09 — J: nombres de archivo y `id` de changeset (a revisar, sin TODO)

- Sin sufijo `_NNN`: `20260910_0001_alter_lenguages_sequence_increment.yaml`
  (además typo `lenguages` → `languages`),
  `20260911_0001_alter_languages_code_length.yaml`,
  `20260916_0001_create_permission.yaml`, `20260916_0002_create_user.yaml`,
  `20260916_0003_create_user_profile.yaml`, `20260916_0004_create_user_types.yaml`.
  Los archivos no se pueden renombrar sin romper checksums de BD ya migradas;
  si la BD es solo local, renombrar es seguro; si ya corrió en compartida,
  documentar la excepción y aplicar el formato solo a archivos nuevos.
- Regla “`id` = filename sin extensión”: solo vale para el **primer** changeset
  de archivos conformes; los siguientes añaden sufijos (`_indexes_002`,
  `_revision_003`). Casos a normalizar en el texto: `20260911...` tiene un `id`
  totalmente distinto al archivo; `20260916_0001...` usa plural
  (`..._create_permissions_001`) vs singular del archivo.

## Omitido a petición tuya

- Java 24 (doc) vs Java 25 (entorno): Gradle falla con
  `IllegalArgumentException: 25` en `JavaVersion.parse`. Lo dejas así por ahora.
