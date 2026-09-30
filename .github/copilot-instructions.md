# Contexto y Convenciones del Proyecto: Factura Cart Microservices

## 1. Arquitectura General y Tecnologías Core
- **Estructura Gradle:** Monorepo con Gradle Multi-Project Build. Las dependencias están centralizadas **estrictamente** en `gradle/libs.versions.toml`. La configuración se modulariza a través de plugins de convención escritos en Kotlin DSL (`buildSrc/src/main/kotlin/*.gradle.kts`). Los archivos `build.gradle` de los módulos no deben declarar versiones explícitas.
- **Lenguaje:** Java 24 puro (sin Kotlin en el código fuente de los microservicios).
- **Boilerplate y Mapeo:** Uso de Lombok y MapStruct, con versiones centralizadas exclusivamente en `gradle/libs.versions.toml` (nunca declaradas en los `build.gradle` de los módulos). El compilador incluye las banderas `--add-opens` necesarias para el procesamiento del AST en Java 24.
- **Framework Principal:** Spring Boot 4.1.1. **Regla de oro:** Todas las importaciones de persistencia, validación y servlets deben usar el paquete `jakarta.*` (ej. `jakarta.persistence.Entity`, `jakarta.validation.constraints.NotNull`); NUNCA usar `javax.*`.
- **Base de Datos y Persistencia:** PostgreSQL con Spring Data JPA. El versionado y estructura de la BD se gestionan de manera exclusiva mediante migraciones de Liquibase (`spring-boot-starter-liquibase`).
- **API Gateway (Capa Reactiva):** Spring Cloud Gateway basado en WebFlux (Netty). Está **estrictamente prohibido** sugerir o agregar `spring-boot-starter-web` (Tomcat), dependencias MVC o repositorios bloqueantes (JPA) en este módulo.
- **Microservicios Backend (Capa Bloqueante):** Basados en Spring Web MVC (Tomcat / Servlets clásicos). Heredan sus configuraciones aplicando los plugins de convención correspondientes (`spring-web-conventions`, `spring-jpa-conventions`, `migration-conventions`).

## 2. Arquitectura de Módulos y Patrón Abstraído (Web MVC vs WebFlux)

Todos los microservicios backend deben seguir el patrón de abstracción genérica para entidades, mappers, repositorios, reglas de negocio, servicios y controladores, respetando estrictamente el stack de ejecución asignado al módulo. Las clases base descritas en la sección D (`BaseRepository`, `BaseService`, `BaseController`, `RestResponse`) son **exclusivas de módulos bloqueantes MVC/JPA (sección A)**. Los módulos reactivos (sección B) deben usar equivalentes `Reactive*` propios (ej. `ReactiveBaseService` retornando `Mono`/`Flux`, repositorios R2DBC); está prohibido reutilizar las clases base MVC en código reactivo.

### A. Módulos Basados en Spring Web MVC (Bloqueantes / Servlets)
- **Ámbito:** Microservicios con lógica de negocio tradicional y persistencia relacional (`user-auth`, `facturacion`, etc.).
- **Contrato de Firma:** Métodos de servicios y controladores deben retornar tipos síncronos directos (`T`, `List<T>`, `Optional<T>`, `ResponseEntity<T>`).
- **Aislamiento:** Está **estrictamente prohibido** importar dependencias reactivas (`spring-boot-starter-webflux`, `reactor-test`, `Mono`, `Flux`) en el código de producción (`src/main`) de estos módulos.
- **Excepción en tests (`src/test`):** Se permite `spring-boot-starter-webflux` únicamente como cliente de pruebas (`WebTestClient`). Sigue prohibido exponer tipos reactivos (`Mono`, `Flux`) en firmas de producción.

### B. Módulos Basados en Spring WebFlux (Reactivos / No Bloqueantes)
- **Ámbito:** API Gateway (`api`) y microservicios de alto rendimiento o streaming de eventos.
- **Contrato de Firma:** Métodos de controladores, filtros y servicios deben retornar **obligatoriamente** tipos de Project Reactor (`Mono<T>` para 0..1 elementos o `Flux<T>` para 0..N elementos).
- **Persistencia y Capa de Datos:** Uso de Spring Data R2DBC o conectores no bloqueantes. Nunca bloquear el Event Loop de Netty con chamadas JDBC síncronas o JPA.
- **Aislamiento:** Está **estrictamente prohibido** importar la dependencia bloqueante `spring-boot-starter-web` (Tomcat/MVC) en módulos reactivos para evitar conflictos de autoconfiguración.

### C. Abstracciones Genéricas Permitidas
- Mantenimiento de clases base genéricas en `shared-kernel` para DTOs, Mappers (MapStruct), Utilidades de traducción/i18n y Reglas de Negocio base, asegurando que no fuercen imports de Spring MVC en código reactivo ni viceversa.

### D. Persistencia (Entities & Repositories) — Exclusivo de módulos MVC/JPA (sección A)
- **`AuditableEntity`:** `@MappedSuperclass` abstracta con auditoría Spring Data (`createdAt`, `updatedAt`, `createdBy`, `updatedBy`). No incluye clave primaria.
- **`IdentifiableImpl<ID>`:** Contrato (`getId`/`setId`) que desacopla el tipo de identificador de la auditoría.
> **Nota de nomenclatura:** los contratos terminados en `Impl` (`IdentifiableImpl`, `DaoRulesImpl`, `BaseControllerImpl`) son **interfaces**, no clases de implementación; no renombrarlos ni generar clases concretas basándose en ese sufijo.
- **`LongAuditableEntity` / `UUIDAuditableEntity`:** `@MappedSuperclass` que extienden `AuditableEntity` e implementan `IdentifiableImpl<ID>` con `@Id` y `@GeneratedValue` concretos (`Long`/`AUTO` o `UUID`/`UUID`). Elegir la superclase según el tipo de clave requerido por la entidad final. Prohibido redefinir `id` o auditoría en entidades concretas.
- **`BaseCatalogEntity`:** Extiende `LongAuditableEntity` para entidades de catálogo con `code`, `active` y `name`.
- **`BaseCatalogTranslationEntity<C extends BaseCatalogEntity>`:** Extiende `LongAuditableEntity`; modela traducciones i18n de un catálogo (`catalog`, `language`, `translation`, `description`). Usar este patrón para todo catálogo que requiera soporte multi-idioma.
- **`Language`:** Entidad concreta de `shared-kernel` (extiende `BaseCatalogEntity` + `isDefault`) usada como referencia de idioma en las traducciones.
- **`BaseRepository<E, ID>`:** Interfaz genérica `@NoRepositoryBean` que extiende `JpaRepository<E, ID>` y `RevisionRepository<E, ID, Integer>` (auditoría/versionado con Envers); provee `getAllAscending()`, `getAllDescending()`.
- **`BaseCatalogRepository<E, ID>`:** Extiende `BaseRepository<E, ID>`; añade `existsByCode`, `findByCode` (`@Transactional(readOnly=true)`), `existsByName`, `findByName` (`@Transactional(readOnly=true)`).
- **`BaseCatalogTranslationRepository<E extends BaseCatalogTranslationEntity<C>, C extends BaseCatalogEntity>`:** Extiende `BaseRepository<E, Long>`; añade `findByCatalogAndLanguage`, `countByCatalog`, `findByCatalog`.
- **Regla:** entidades concretas solo `@Entity`/`@Table` + constructor a `super()`.

### E. Mapeo y DTOs (MapStruct)
- **DTOs:** Definidos mediante Java **`record`** con validaciones de `@jakarta.validation`.
- **`BaseMapper<E, D>`:** Interfaz genérica para mapeos bidireccionales (`toDto`, `toEntity`, listas).
- **Configuración MapStruct:** Usar interfaz `@MapperConfig` centralizada (`componentModel = "spring"`, `unmappedTargetPolicy = ReportingPolicy.IGNORE`).

### F. Reglas de Negocio (Validation Layer)
- **`DaoRulesImpl<E>`:** Contrato de validación (`canSave`, `canUpdate`, `canDelete`).
- **`BaseDaoRules<E, R, ID>`:** Implementación base; valida existencia por ID y unicidad de nuevo registro (`isNew`, `hasId`, `existsById`).
- **`BaseDaoCatalogRules<E, R>`:** Extiende `BaseDaoRules<E, R, Long>`; añade validación de `code` no vacío y único para entidades de catálogo.
- **`BaseDaoCatalogTranslationRules<E, R, C>`:** Extiende `BaseDaoRules<E, R, Long>`; valida que no exista una traducción duplicada por catálogo+idioma y exige mínimo dos traducciones antes de permitir un borrado.
- Excepciones: `BusinessRuleException` (violación de regla de negocio) y `EntityNotFoundException` (entidad inexistente), ambas heredan de `BaseException` con un código de mensaje i18n.
- **Convención de códigos de error:** prefijo por dominio + número, ej. `Ent-XXX` (genérico de entidad), `Cat-XXX` (catálogo), `Lan-XXX` (idioma/traducción). Resueltos vía `messages.properties` (`i18n/messages`).

### G. Capa de Servicio (Service Layer)
- **`BaseService<E extends IdentifiableImpl<ID>, D extends IdentifiableDtoImpl<ID>, ID, R extends BaseRepository<E, ID>>`:** Clase abstracta que recibe `repository`, `mapper` y `rules` por constructor (no como parámetros de tipo).
- Expone operaciones CRUD transaccionales (`@Transactional`) con **sobrecarga** de métodos para Entidad y DTO: `findAllDto()`, `findDtoById(ID)`, `save(D)` / `save(E)`, `update(D)` / `update(E)`, `deleteById(ID)`, además de variantes ascendente/descendente (`findAllAscendingDto`, `findAllDescendingDto`).
- **`BaseCatalogService<E, D, R>`:** Extiende `BaseService<E, D, Long, R>`; añade `findByCode` / `findDtoByCode`.
- **`BaseCatalogTranslationService<E, D, R, C>`:** Extiende `BaseService<E, D, Long, R>`; añade `findByCatalog`, `findByCatalogDto`, `findByCatalogAndLanguage`, `findByCatalogAndLanguageDto`.

### H. Capa de Control (REST Controllers)
- **Segregación por Interfaces:** `ReadController<ID, DTO>`, `WriteController<ID, DTO>`, `UpdateController<ID, DTO>`, `DeleteController<ID, DTO>` — cada una con métodos `default` que exponen `@GetMapping`/`@PostMapping`/`@PutMapping`/`@DeleteMapping`, envolviendo la respuesta en `RestResponse<T>`.
- **`BaseControllerImpl<ID, DTO>`:** Contrato raíz con `getService()`. **`BaseController<ID, DTO>`:** Implementación base que inyecta el `BaseService` por constructor.
- Mensajes de éxito i18n resueltos vía `MessageResolver.resolveMessage(code)` (`S-000` crear, `S-001` actualizar, `S-002` eliminar).
- **`GlobalExceptionHandler`** (`@ControllerAdvice` en `shared-kernel`): captura `EntityNotFoundException`, `BusinessRuleException`, `NoResourceFoundException` y excepciones genéricas, devolviendo siempre `RestResponse` con código HTTP, código de negocio y mensaje.

### I. Convenciones de Pruebas
- **Pruebas unitarias de servicio** (`src/test/.../service/*ServiceTest.java`): `@ExtendWith(MockitoExtension.class)`, `@Mock` para repositorio y mapper, `@InjectMocks` para el servicio bajo prueba. No se levanta contexto de Spring.
- **Pruebas unitarias de mapper** (`src/test/.../mappers/*MapperTest.java`): instancian el mapper vía `Mappers.getMapper(XxxMapper.class)` (sin contexto Spring), usando `Instancio` para generar datos aleatorios y `Select.field(...)` para fijar valores puntuales de verificación.
- **Pruebas de integración de repositorio** (`src/test/.../repository/*RepositoryTest.java`): `@DataJpaTest`, `@ActiveProfiles("test")`, `@Import(AuditConfig.class)`, `@AutoConfigureTestDatabase(replace = Replace.NONE)` (usa la BD real de test, no H2), `TestEntityManager` para `persistAndFlush`. Evitar `Instancio.create(Entity.class)` para el ID/PK: usar el constructor de negocio o limpiar el `id` explícitamente antes de persistir.
- **Pruebas de integración de controlador** (`src/test/.../controller/*IntegrationTest.java`): `@SpringBootTest(webEnvironment = RANDOM_PORT)`, `@ActiveProfiles("test")`, `@LocalServerPort`, peticiones HTTP reales vía `RestAssured`.
- El perfil `test` (ver `application.yml`) desactiva Liquibase, sesión y caché para aislar las pruebas.

### J. Migraciones (Liquibase)
- Cada microservicio bloqueante tiene su propio `src/main/resources/db/migration-root.yaml`, que solo contiene un `includeAll` apuntando a `migrations/` (`relativeToChangelogFile: true`, `errorIfMissingOrEmpty: true`). No declarar changesets directamente en el root.
- **Convención de nombres de archivo:** `YYYYMMDD_NNNN_descripcion_NNN.yaml` (fecha, secuencia del día de 4 dígitos, descripción en snake_case, secuencia de changeset de 3 dígitos). Varios changesets del mismo día incrementan ambas secuencias.
- **Convención de `id` del changeSet:** igual al nombre de archivo sin extensión.
- Cada tabla de catálogo/entidad con PK `Long` debe crear su propia secuencia (`createSequence`) y usarla en la columna `id` vía `defaultValueSequenceNext`.
- Las tablas de catálogo replican los campos de `BaseCatalogEntity` (`code` único, `active`, `name` único) y las de traducción los de `BaseCatalogTranslationEntity` (`catalog_id`, `language_id`, `translation`, `description`).
- Envers requiere la tabla `revinfo` (ver `20260907_0001_create_revinfo_001.yaml`) como primer changeset del proyecto.

### K. Configuración (`application.yml`) — Microservicios Bloqueantes (MVC/JPA)
Estructura de referencia (`user-auth`), replicable en todo microservicio MVC nuevo:
- `spring.application.name`: nombre del microservicio.
- `spring.datasource.*`: `url`/`username`/`password` parametrizados con variables de entorno y valor por defecto (`${VAR:default}`), nunca credenciales hardcodeadas sin fallback de desarrollo.
- `spring.liquibase.change-log`: siempre `classpath:db/migration-root.yaml`; `enabled` controlado por `${LIQUIBASE_ENABLED:false}`.
- `spring.jpa.hibernate.ddl-auto: validate` (el esquema lo gestiona Liquibase, JPA solo valida), `dialect: PostgreSQLDialect`, `physical_naming_strategy: CamelCaseToUnderscoresNamingStrategy`, y bloque `envers` (`audit_table_suffix`, `revision_field_name`, `revision_type_field_name`, `store_data_at_delete`).
- `spring.mvc.problem.include-stacktrace: always` (solo entornos de desarrollo/diagnóstico).
- `server.port`: parametrizado por variable de entorno propia del servicio (ej. `${PORT_USER_AUTH:8081}`).
- Documentos de perfil (`---`) separados: perfil `test` (Liquibase off, sesión y caché `none`) y perfil `task` (`web-application-type: none`, para ejecución CLI como tareas de Liquibase).
- **Nota para el API Gateway (WebFlux):** esta plantilla no aplica tal cual — no tiene `datasource`/`jpa`/`liquibase`; en su lugar define `spring.cloud.gateway` (rutas) y variables de host/puerto de los servicios downstream.

## 3. Convenciones de Código
- Usar Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`) en entidades y servicios para mantener el código limpio.
- Respuestas HTTP estandarizadas envolviendo las salidas en `RestResponse<T>`.
- Manejo de internacionalización (i18n) en mensajes de error mediante archivos `messages.properties` y la cabecera `Accept-Language`.
- Seguir las convenciones de nombres de paquetes y clases establecidas en el proyecto para mantener la coherencia y facilitar la navegación del código.
- Todo comentario, Javadoc e identificador en el código debe ser descriptivo y estar escrito en inglés (evitar abreviaturas innecesarias); las explicaciones al usuario en el chat deben ser en español.

# 4. Buenas Prácticas Adicionales
- Mantener la consistencia en el estilo de codificación en todo el proyecto.
- Evitar la duplicación de código mediante la reutilización de componentes genéricos.
- Escribir pruebas unitarias y de integración para asegurar la calidad del código.
- Documentar las APIs REST utilizando herramientas como Swagger/OpenAPI.
- Realizar revisiones de código periódicas para asegurar la calidad y consistencia del código.
- Mantener actualizada la documentación del proyecto para facilitar la incorporación de nuevos desarrolladores.
- Seguir las mejores prácticas de seguridad, como la gestión adecuada de credenciales y la protección de datos sensibles.
- Mantener las dependencias del proyecto actualizadas para evitar vulnerabilidades de seguridad conocidas.
