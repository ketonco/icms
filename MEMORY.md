# MEMORY.md - Memoria Técnica del Proyecto ICMS

> Alcance: solo decisiones esenciales del proyecto para futuros ajustes.
> No es bitácora de tareas: el historial vive en git y los pendientes activos
> en `pending.md`. Tamaño máximo: 250 líneas; resumir o eliminar lo que deje
> de aportar.
>
> Regla operativa inviolable: nunca ejecutar ni modificar nada sin preguntar
> antes y mostrar el contenido exacto propuesto (plan + bloque/diff con
> `archivo:línea`); solo actuar con autorización explícita del desarrollador.
> Ni siquiera `pending.md` o `MEMORY.md` se tocan a primeras.

---

## 1. Estado Actual del Proyecto

- **Arquitectura Backend:** Monorepo multimódulo en Java 24 y Spring Boot 3.x sin frontend integrado (cliente basado en tests unitarios y de integración).
- **Estructura de Módulos:**
  - `:shared-kernel`: Librería Java interna con entidades base, DTOs, mappers (MapStruct) y excepciones globales.
  - `:api`: API Gateway reactivo con Spring Cloud Gateway y Spring WebFlux (Netty).
  - `:user-auth`: Microservicio de autenticación, usuarios y seguridad basado en Spring Web MVC, Spring Data JPA y Liquibase.
- **Etapa de Desarrollo Activa:**
  - Implementación de la entidad `Usuario` y sus funcionalidades asociadas en `:user-auth`.
  - Construcción y configuración del módulo de seguridad (Spring Security).
  - Estandarización de la suite de pruebas (unitarias e integración).
  - Optimización y diseño de DTOs ajustados por roles y proyecciones de consultas.
- **Gestión de Construcción:**
  - Centralización de dependencias parcialmente finalizada utilizando `gradle/libs.versions.toml` y plugins de convención en `buildSrc/`.

---

## 2. Decisiones Arquitectónicas (y el Porqué)

- **Aislamiento de Pilas (Reactive vs Servlet):**
  - *Decisión:* Mantenimiento estricto de WebFlux en `:api` y Web MVC (Servlet) en `:user-auth` (`src/main`); `starter-webflux` permitido solo en `src/test` como cliente `WebTestClient`.
  - *Por qué:* Previene bloqueos de hilos Netty y conflictos de dependencias entre Tomcat y Netty.
- **DTOs Basados en Roles y Proyecciones:**
  - *Decisión:* Separación explícita entre DTOs de entrada/salida y DTOs según la jerarquía de roles (Admin, Usuario, etc.).
  - *Por qué:* Evita la sobreexposición de datos sensibles (campos como contraseñas hash, tokens o metadatos internos) y optimiza las consultas SQL.
- **Java 24 + Spring Boot 3.x:**
  - *Decisión:* Mantener compilación y toolchain en Java 24 aplicando banderas `--add-opens` en `JavaCompile`.
  - *Por qué:* Permite explorar las últimas características de Java mientras se otorga acceso por reflexión a Lombok y MapStruct sobre el AST.
- **Gestión de BD y Migraciones con Liquibase:**
  - *Decisión:* Control de esquemas PostgreSQL mediante changelogs YAML en `:user-auth`, con secuencias explícitas (`defaultValueSequenceNext`, patrón de `20260909_0001_create_languages_001.yaml`).
  - *Por qué:* Garantiza trazabilidad y reproducibilidad del esquema de BD en entornos locales y de CI/CD.
- **Estrategia Futura de Automatización e IA:**
  - *Decisión:* Integración planeada de n8n (Docker), Spring AI / Ollama y WhatsApp Cloud API en modo Sandbox.
  - *Por qué:* Permitirá simular flujos de comercio conversacional y pruebas de carrito/facturación sin costo de infraestructura.

---

## 3. Aprendizajes y Errores a Evitar

- **Falsas Dependencias en Spring Boot:**
  - *Aprendizaje:* Spring Boot no ofrece starters de prueba individuales como `spring-boot-starter-data-jpa-test` o `spring-boot-starter-webmvc-test`. Toda la suite de prueba autoconfigurada proviene de `spring-boot-starter-test` y `spring-security-test`.
- **Incompatibilidad de Imports (`javax` vs `jakarta`):**
  - *Aprendizaje:* En Spring Boot 3.x / Jakarta EE es obligatorio usar `jakarta.persistence.*` y `jakarta.validation.*`. El uso de `javax.*` genera fallos de compilación o escaneo de anotaciones.
- **Duplicidad en Módulos de Gradle:**
  - *Aprendizaje:* Declarar dependencias presentes en `implementation` dentro del bloque `testImplementation` es redundante, ya que el *classpath* de pruebas hereda automáticamente la configuración principal.
- **Comparación `Long` con `!=`:**
  - *Aprendizaje:* Comparar IDs `Long` con `!=` compara referencias y da falsos negativos fuera del rango cacheado; usar siempre `equals`.
- **Secuencias huérfanas en Liquibase:**
  - *Aprendizaje:* `autoIncrement: true` y `defaultValueSequenceNext` son excluyentes por columna; crear `createSequence` + `autoIncrement` deja la secuencia huérfana.
- **Plantilla de migraciones:**
  - *Decisión:* `1guides/09-plantilla-migracion.md` es la guía copia/pega (tabla + `_aud` Envers + índices; relacionales sin `_aud`).
  - *Por qué:* Nace de P-02 para evitar repetir las inconsistencias de secuencias e índices.
- **Comando `/new-migration`:**
  - *Decisión:* `.opencode/commands/new-migration.md` (agente `plan`) genera migraciones desde la clase entidad: valida `@Entity` + `@Table(name)`, deriva columnas de la herencia y detecta el módulo/BD por paquete.
  - *Por qué:* Cada microservicio tendrá su BD propia; el comando evita asumir `user-auth` y frena si falta `@Table(name)`.
- **Comando `/update-guides`:**
  - *Decisión:* `.opencode/commands/update-guides.md` (agente `plan`) verifica o propone guías en `1guides/` por tema; con `all` solo lista las desactualizadas sin tocarlas.
  - *Por qué:* Las guías se revisan 1 a 1 y cada una se actualiza con el mismo comando.
- **Skills Codex (referencia):**
  - *Decisión:* `.agents/skills/new-migration/SKILL.md` y `.agents/skills/update-guides/SKILL.md` replican ambos comandos para Codex (`name` + `description` en inglés, cuerpo en español); se invocan con `$` o por coincidencia.
  - *Por qué:* Codex no usa comandos `/` ni `$ARGUMENTS`; las skills son su formato nativo de repo (`.agents/skills`).
- **Skill `update-pending` (Codex audita, equipo ejecuta):**
  - *Decisión:* `.agents/skills/update-pending/SKILL.md` revisa código contra instrucciones y estándares, registra en `pending.md` con plantilla y marca `TODO`s. Solo registra, no corrige.
  - *Por qué:* Las revisiones se delegan a Codex; las tareas de código quedan en nuestras sesiones.
- **Comandos por ámbito (`commands/`):**
  - *Decisión:* `commands.md` es índice; cada ámbito/módulo tiene su archivo (`gradle`, `user-auth`, `user-auth-tests`, `shared-kernel`, `api`). Tests y seeds se documentan como patrón (`--tests`, `--seed=`), no enumerados.
  - *Por qué:* El archivo único crecía por cada test/seed/módulo nuevo; parametrizar + dividir evita ediciones constantes.

---

## 4. Próximos Pasos

1. **Entidad `Usuario` y Repositorios:** Finalizar el modelado de la entidad `Usuario`, sus enums/roles y la capa de persistencia con Spring Data JPA.
2. **Módulo de Seguridad (Spring Security):** Implementar la configuración de seguridad, gestión de tokens (JWT/OAuth2) y filtros de autenticación/autorización.
3. **Estandarización de Pruebas:** Definir las clases base y patrones para tests unitarios (Mockito/AssertJ/Instancio) y de integración (`@SpringBootTest` / `@DataJpaTest`).
4. **Optimización de DTOs y Mappers:** Diseñar la jerarquía de DTOs según el rol del usuario y mapeadores con MapStruct.
