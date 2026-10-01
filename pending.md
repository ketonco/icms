# PENDING.md — Pendientes activos del proyecto ICMS

Validación de `.github/copilot-instructions.md` vs código real. Fecha: 2026-09-30.
Solo lectura y marcadores `TODO`; nada de lo listado está corregido todavía.

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

## P-10 — Ninguna entidad lleva `@Audited`: Envers cableado pero inoperante

**Dónde:** las 8 entidades (`shared-kernel/.../entity/Language.java`,
`user-auth/.../entity/User.java`, `UserProfile.java`, `UserStatus.java`,
`UserStatusTranslation.java`, `UserType.java`, `UserTypeTranslation.java`,
`Permission.java`)

**Ubicacion del TODO: no agregado** (requiere tu decisión antes de marcar código)

**Problema:** Envers está cableado en las tres capas menos en la que lo activa:
`BaseRepository` extiende `RevisionRepository`, existen `revinfo` y todas las
tablas `_aud` (`languages_aud`, `user_types_aud`, etc.), y `User.java:77`
declara `@AuditJoinTable(name = "user_types_aud")` — pero **ninguna** de las 8
entidades tiene `@Audited` (`grep Audited` en `src/` devuelve 0 resultados).
Sin `@Audited`, Envers no escribe ni una fila: toda la auditoría versionada es
peso muerto.

**Contexto / Explicación:** `@Audited` es el interruptor de Envers por entidad;
`@AuditJoinTable` solo nombra la tabla de la colección auditada y también exige
entidad auditada. El `@Builder` sí está en las 8/8 (verificado), y la auditoría
Spring Data (`createdAt/...`, sección D) funciona sin Envers — son dos
auditorías distintas y hoy solo vive la segunda.

**Opciones estándar:**

- A) Auditar (recomendado si se exige historial): añadir `@Audited` a las 7
  entidades de dominio (`User`, `UserProfile`, catálogos y traducciones;
  decidir si `Language` también) y documentar la regla en la sección D.
- B) No auditar: retirar Envers (`RevisionRepository` → `JpaRepository`,
  quitar `@AuditJoinTable`, eliminar `_aud`/`revinfo` con changesets nuevos).

**Recomendación:** A — la inversión en `_aud`/`revinfo` ya está hecha en BD;
solo falta el interruptor Java más una línea en la instrucción.

**Nivel de acción requerido:** Alto — la auditoría versionada, objetivo
declarado del diseño, hoy no registra nada.

## Nota — Omitido a petición del desarrollador

- Java 24 (doc) vs Java 25 (entorno): Gradle falla con
  `IllegalArgumentException: 25` en `JavaVersion.parse`. Se deja así por ahora.

## P-11 — Falta una prueba para la configuración global de MapStruct

**Dónde:** `shared-kernel/src/main/java/com/icms/shared/config/mapper/MapperSetting.java:13`

**Ubicacion del TODO: agregado** en `MapperSetting.java:13`.

**Problema:** no hay una prueba dedicada que compruebe que la configuración compartida de MapStruct ignora destinos sin mapear y que la estrategia `IGNORE` para valores `null` conserva los valores existentes en la entidad.

**Contexto / Explicación:** `MapperSetting` configura el comportamiento global de los mapeadores; una regresión puede afectar los mapeos de todos los módulos que la reutilizan.

**Opciones estándar:**

- A) Añadir una prueba de mapeador que cubra ambos comportamientos (recomendado).
- B) Validar el comportamiento únicamente mediante las pruebas de cada mapeador consumidor.

**Recomendación:** A, para verificar la configuración compartida directamente.

**Nivel de acción requerido:** Medio — la configuración se comparte y puede afectar múltiples mapeos.

## P-12 — `UserMapperTest` inicializa Mockito manualmente

**Dónde:** `user-auth/src/test/java/com/icms/user_auth/mappers/UserMapperTest.java:34-44`

**Ubicacion del TODO: agregado** en `UserMapperTest.java:43`.

**Problema:** el test inicializa Mockito mediante `MockitoAnnotations.openMocks(this)` en `@BeforeEach`, mientras que `.github/copilot-instructions.md` establece `@ExtendWith(MockitoExtension.class)` para pruebas unitarias con Mockito.

**Contexto / Explicación:** el TODO anterior era genérico y no definía qué debía validarse; el patrón indicado evita la inicialización manual de los mocks.

**Opciones estándar:**

- A) Usar `@ExtendWith(MockitoExtension.class)` y retirar `@BeforeEach` con `openMocks` (recomendado).
- B) Mantener la inicialización manual y documentar una excepción para esta prueba.

**Recomendación:** A, para alinear el test con la convención del proyecto.

**Nivel de acción requerido:** Bajo — consistencia de pruebas, sin impacto en producción.

## P-13 — Contraseña de base de datos predeterminada en configuración

**Dónde:** `user-auth/src/main/resources/application.yml:8` y
`user-auth/build.gradle:46`

**Ubicacion del TODO: agregado** en ambos valores de contraseña
predeterminados.

**Problema:** la configuración de ejecución y la actividad Liquibase contienen
un valor de contraseña fijo como alternativa cuando no se proporciona
`DB_PASSWORD` o `dbPassword`. `AGENTS.md` prohíbe incluir contraseñas
hardcodeadas en archivos de configuración que no sean de ejemplo.

**Contexto / Explicación:** si una ejecución no establece explícitamente la
variable o propiedad, la aplicación o Liquibase intentarán autenticarse con la
contraseña predeterminada. El valor versionado no debe servir como credencial
de entornos compartidos.

**Opciones estándar:**

- A) Exigir la variable o propiedad fuera de los entornos locales y mantener
  los valores locales fuera de la configuración versionada (recomendado).
- B) Mover los valores de desarrollo a un archivo de ejemplo no usado
  directamente por la aplicación.

**Recomendación:** A, para que las ejecuciones no locales fallen si falta una
credencial externa.

**Nivel de acción requerido:** Alto — evita el uso accidental de una
contraseña fija en entornos no locales.

## P-14 — Stack traces habilitados en la configuración base

**Dónde:** `user-auth/src/main/resources/application.yml:26`

**Ubicacion del TODO: agregado** en la propiedad
`spring.mvc.problem.include-stacktrace`.

**Problema:** `include-stacktrace: always` está declarado en la configuración
base y no limitado a perfiles de desarrollo o diagnóstico, como requiere la
sección K de `.github/copilot-instructions.md`.

**Contexto / Explicación:** los perfiles activos heredan la configuración
base; las respuestas de error podrían revelar rutas internas, nombres de
clases y detalles de implementación.

**Opciones estándar:**

- A) Activar los stack traces solo mediante una configuración de perfil de
  desarrollo o diagnóstico (recomendado).
- B) Deshabilitarlos en la configuración base y habilitarlos explícitamente al depurar.

**Recomendación:** A, manteniendo el diagnóstico detallado fuera de perfiles
compartidos o productivos.

**Nivel de acción requerido:** Alto — puede exponer información interna en
respuestas de error.

## P-15 — Rutas protegidas sin mecanismo de autenticación visible

**Dónde:**
`user-auth/src/main/java/com/icms/user_auth/config/SecurityConfig.java:46-50`

**Ubicacion del TODO: agregado** antes de deshabilitar login por formulario y
HTTP Basic.

**Problema:** la configuración requiere autenticación para las rutas no
públicas, pero deshabilita form login y HTTP Basic. En `user-auth` no se
encontró un `AuthenticationProvider`, un `UserDetailsService` ni otro
mecanismo de autenticación configurado.

**Contexto / Explicación:** las solicitudes sin una identidad autenticada
reciben rechazo, y el módulo no presenta un flujo visible para establecer esa
identidad. La configuración de autorización por sí sola no permite
autenticar usuarios.

**Opciones estándar:**

- A) Implementar el mecanismo de autenticación previsto y conectarlo a esta
  cadena de filtros (recomendado).
- B) Habilitar temporalmente un mecanismo soportado por Spring Security
  mientras se implementa el flujo definitivo.

**Recomendación:** A, definiendo el mecanismo previsto antes de abrir las
rutas protegidas.

**Nivel de acción requerido:** Alto — impide el acceso autenticado a las rutas
protegidas y deja incompleto el flujo central del módulo.
