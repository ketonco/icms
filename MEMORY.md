# MEMORY.md - Memoria Técnica del Proyecto ICMS

> Alcance: solo decisiones esenciales del proyecto para futuros ajustes.
> No es bitácora de tareas: el historial vive en git y los pendientes activos
> en `pending.md` en minúsculas. El duplicado `PENDING.md` en mayúsculas es
> histórico y lo elimina el desarrollador con Git.
> Tamaño máximo: 250 líneas; resumir o eliminar lo que deje de aportar.
>
> Precedencia: `.github/copilot-instructions.md` prevalece sobre el código real,
> que prevalece sobre `AGENTS.md`, que prevalece sobre este archivo.
> Lo ya implementado en el esqueleto es ley.
>
> Regla operativa: la inspección de solo lectura con `read`, `glob` y `grep`
> está siempre permitida. `shell`, `edit` y `write` requieren plan previo con
> `archivo:línea` y autorización explícita. `pending.md` y `MEMORY.md` solo se
> tocan sin pregunta extra cuando la tarea, skill o comando en curso lo ordena.

---

## 1. Estado Actual del Proyecto

- **Arquitectura Backend:** Monorepo multimódulo en Java 24 y Spring Boot 4.1.1 según `gradle/libs.versions.toml:2-3`, sin frontend integrado.
- **Estructura de Módulos:**
  - `:shared-kernel`: Librería Java interna con entidades base, DTOs, mappers MapStruct y excepciones globales.
  - `:api`: API Gateway reactivo con Spring Cloud Gateway y Spring WebFlux sobre Netty, sin persistencia propia.
  - `:user-auth`: Microservicio de autenticación, usuarios y seguridad con Spring Web MVC, Spring Data JPA y Liquibase.
- **Etapa de Desarrollo Activa:**
  - Implementación de la entidad `Usuario` y sus funcionalidades asociadas en `:user-auth`.
  - Construcción y configuración del módulo de seguridad con Spring Security.
  - Estandarización de la suite de pruebas unitarias y de integración.
  - Optimización y diseño de DTOs ajustados por roles y proyecciones de consultas.
- **Gestión de Construcción:**
  - Catálogo centralizado en `gradle/libs.versions.toml` y plugins Kotlin DSL en `buildSrc/src/main/kotlin/`.
  - Excepción conocida P-03: quedan dos versiones fuera del catálogo, `mavenBom 4.1.1` y `picocli 4.7.6`, pendientes de decisión del desarrollador.
- **Comandos canónicos:** `.opencode/commands/` en inglés. La carpeta `commands/` en raíz es legado. Guías en español en `1guides/`.
- **Skill QA de Codex:**
  - *Decisión:* `.agents/skills/qa-review/SKILL.md` es la única skill de QA (absorbió a `update-pending`): revisa arquitectura, código y tests, usa `qa_historic.md` para revisar solo desde la última corrida y solo puede escribir `pending.md`, `qa_historic.md` y `MEMORY.md`.
  - *Por qué:* Un solo punto de entrada QA; los tests commiteados se asumen pasados y los tres archivos fuente quedan protegidos.

---

## 2. Decisiones Arquitectónicas y el Porqué

- **Aislamiento de Pilas Reactive contra Servlet:**
  - *Decisión:* WebFlux en `:api` y Web MVC en `:user-auth` en `src/main`; `starter-webflux` solo en `src/test` como cliente `WebTestClient`.
  - *Por qué:* Previene bloqueos del Event Loop de Netty y conflictos entre Tomcat y Netty.
- **Gateway sin Persistencia:**
  - *Decisión:* `:api` solo enruta por HTTP no bloqueante. R2DBC solo si un módulo reactivo necesitara base de datos.
  - *Por qué:* Evita exigir una dependencia que hoy nada usa.
- **DTOs Basados en Roles y Proyecciones:**
  - *Decisión:* Separación explícita entre DTOs de entrada y salida según jerarquía de roles.
  - *Por qué:* Evita sobreexposición de datos sensibles y optimiza consultas SQL.
- **Java 24 y Spring Boot 4.1.1:**
  - *Decisión:* Toolchain en Java 24 con banderas `--add-opens` en `JavaCompile` y versiones en `libs.versions.toml`.
  - *Por qué:* Explora el Java actual con acceso por reflexión para Lombok y MapStruct sobre el AST.
- **Gestión de BD con Liquibase:**
  - *Decisión:* Esquema PostgreSQL con changelogs YAML en `:user-auth` y secuencias explícitas.
  - *Por qué:* Garantiza trazabilidad y reproducibilidad en local y CI-CD.
  - *Flujo local de desarrollo:* las migraciones pueden cambiar repetidamente;
    se reinicia la base local con `dropAll` y luego `update` para aplicar el
    estado actual del changelog.
  - *Alcance:* este flujo aplica a la base local de desarrollo, no a entornos
    compartidos o productivos.
- **Estrategia Futura de IA:**
  - *Decisión:* n8n en Docker, Spring AI con Ollama, WhatsApp Cloud API y Stripe Sandbox.
  - *Por qué:* Simula comercio conversacional y carrito o facturación sin costo de infraestructura.
- **Entorno Java validado en Java 24 (2026-10-02):**
  - *Decisión:* El estándar es Java 24; `JAVA_HOME` (Usuario) y `org.gradle.java.home` (`~/.gradle/gradle.properties`) apuntan a `jdk-24`. El `java` del PATH global sigue en 25 pero no afecta (toolchain `jvmToolchain(24)`).
  - *Por qué:* Con esto Gradle 8.14.3 configura el proyecto sin el fallo `IllegalArgumentException: 25` (validado con `gradlew help` → BUILD SUCCESSFUL).
- **MCP de Postgres para validación:**
  - *Decisión:* `@microsoft/postgres-mcp` vía `opencode.json` (raíz, sin credenciales), con un perfil por módulo (`user-auth` → `ICMS_UA`) y contraseña en el keyring de Windows. Comando `/rebuild-db` ejecuta dropAll + update + seed y valida con el MCP.
  - *Por qué:* Varias BD por microservicio sin duplicar config; cumple la prohibición de contraseñas en archivos versionados.
- **`PasswordEncoder` global sin perfil (2026-10-05):**
  - *Decisión:* El bean vive en `EncryptEncoder` (`@AutoConfiguration` de SharedKernel, sin `@Profile`); no duplicarlo en configs con `@Profile("!task")` como `SecurityConfig`.
  - *Por qué:* El perfil `task` excluye seguridad pero igual instancia servicios que lo exigen; duplicarlo rompe `task` (ausente) o el arranque normal (dos beans). Validado con `/rebuild-db` → seeds verdes.
- **Estándar de nombres de tests: 2 sufijos `*Test`/`*IT` (2026-10-08):**
  - *Decisión:* en todo el monorepo solo dos sufijos para clases de prueba: `*Test` = unitaria (sin contexto Spring completo ni BD real: services, rules, mappers y bundles de `shared-kernel`) y `*IT` = integración (repositorio, controlador, gateway, i18n en contexto, validación de DTO y humo de aplicación). Prohibidos `*UT`, `*IntegrationTest`, `*RepositoryTest` y `*ApplicationTests`. Quedó fijado en `.github/copilot-instructions.md` §I y reflejado en `1guides/11-pruebas.md`.
  - *Por qué:* antes convivían 5 sufijos para 2 categorías reales, con 17 tests de integración repartidos en `*RepositoryTest`, `*IntegrationTest` e `*IT`. `*Test`/`*IT` es la convención Maven Surefire/Failsafe y la única que reconoce el ecosistema (IDEs, SonarQube y filtros CI buscan `*Test`), mientras que `*UT` no la respalda ninguna herramienta. El sufijo no altera la ejecución: `buildSrc/src/main/kotlin/java-common-conventions.gradle.kts` solo declara `useJUnitPlatform()` sin filtro por nombre.
  - *Error a evitar:* no inventar sufijos nuevos ni crear excepciones "solo para este caso"; la uniformidad se logró con 9 `git mv` que alinearon código, fuente de verdad y documentación en una sola pasada. Validado con 173 tests / 1 fallo preexistente (P-31) y `.opencode/refactors/test-naming-standard/test-result.md` → PASS.

---

## 3. Aprendizajes y Errores a Evitar

- **Falsas Dependencias en Spring Boot:**
  - *Aprendizaje:* Spring Boot no ofrece starters de prueba individuales como `spring-boot-starter-data-jpa-test` o `spring-boot-starter-webmvc-test`. La suite autoconfigurada proviene de `spring-boot-starter-test` y `spring-security-test`.
- **Incompatibilidad de Imports `javax` contra `jakarta`:**
  - *Aprendizaje:* En Spring Boot con Jakarta EE es obligatorio usar `jakarta.persistence.*` y `jakarta.validation.*`. El uso de `javax.*` genera fallos de compilación o escaneo.
- **Duplicidad en Módulos de Gradle:**
  - *Aprendizaje:* Declarar en `testImplementation` dependencias ya presentes en `implementation` es redundante, porque el classpath de pruebas hereda la configuración principal.
- **Comparación `Long` con `!=`:**
  - *Aprendizaje:* Comparar IDs `Long` con `!=` compara referencias y da falsos negativos fuera del rango cacheado; usar siempre `equals`.
- **Secuencias huérfanas en Liquibase:**
  - *Aprendizaje:* `autoIncrement: true` y `defaultValueSequenceNext` son excluyentes por columna; crear `createSequence` más `autoIncrement` deja la secuencia huérfana.
- **Beans de `shared-kernel` fuera del escaneo:**
  - *Aprendizaje:* `:user-auth` escanea solo `com.icms.user_auth`, así que los `@Component` de la librería (`MessageResolver`, `GlobalExceptionHandler`, `CustomSecurityExceptionHandler`) solo existen si `AutoConfiguration.imports` los declara como `@Bean`. Sin eso, el `MessageSource` estático de `MessageResolver` queda en `null` y toda respuesta HTTP salía con `"<code> context"` en lugar del texto i18n; lo destapó `MessagesI18nIT`.
  - *Por qué:* decisión convertida en regla permanente y ya movida a `AGENTS.md` §Reglas de dominio: registrar los componentes compartidos en las autoconfiguraciones, nunca confiar en el escaneo de paquetes.

---

## 4. Próximos Pasos

1. **Autenticación y autorización en `user-auth` (P-15):** implementar el
   mecanismo de autenticación previsto, login/token y autorización por
   operación; permitir anónimamente solo lecturas públicas y proteger las
   mutaciones de idiomas y traducciones.
   *Terminado cuando:* las escrituras anónimas responden 401/403, las lecturas
   públicas definidas funcionan y los casos con token válido pasan con tests.
   Este pendiente Alto bloquea QA según la skill `qa-department` mientras siga
   activo.
2. **Completar las traducciones de estados (P-25):** añadir `fr-FR` para ACT,
   INA, SUS, DEL y PEN, ya que el seed declara francés activo.
   *Terminado cuando:* cada estado sembrado tiene traducción en cada idioma
   activo y la integridad se verifica con PostgreSQL MCP.
3. **Completar el contrato de creación de usuario (P-23):** añadir `Location`
   en `POST /api/v1/auth/user` y afirmar HTTP 201, `body.status=201`, datos y
   URI del recurso en `UserControllerIT`.
   *Terminado cuando:* implementación y prueba verifican el contrato completo.
4. **Fortalecer las pruebas HTTP de validación (P-27):** comprobar en
   `UserControllerIT` errores por campo para DTO inválido y que la solicitud
   rechazada no persiste el usuario.
   *Terminado cuando:* los tests fallan si falta el detalle de validación o se
   guarda una solicitud inválida.
5. **Definir el contrato de respuesta vacía (P-31):** elegir entre 204 sin
   cuerpo o un estado que admita `RestResponse`, y cubrir ambos caminos del
   `ReadController`.
   *Terminado cuando:* estado, cuerpo y documentación coinciden en la prueba.
6. **Actualizar PostgreSQL JDBC (P-32):** centralizar la versión corregida
   `42.7.12` o posterior compatible y validar la negociación SCRAM/TLS.
   *Terminado cuando:* el driver resuelve a una versión corregida y la conexión
   de preproducción funciona con TLS verificado.
7. **DTOs por rol con MapStruct:** establecer DTOs de entrada/salida para
   GUE/USR/MOD/ADM con configuración central `MapperSetting`.
   *Terminado cuando:* los DTOs no exponen campos sensibles y los tests de
   mapper verifican los mapeos por rol.
8. **Arrancar `catalog` (F2):** crear el módulo WebMVC con BD propia,
   migraciones Liquibase reutilizando `Base*`, seeds y auditoría.
   *Terminado cuando:* la lectura pública de productos funciona para GUE,
   las operaciones de gestión aplican RBAC y las migraciones y tests pasan.
