# MEMORY.md - Memoria Técnica del Proyecto ICMS

Este archivo almacena la memoria operativa, arquitectura backend, decisiones clave y aprendizajes del proyecto ICMS entre sesiones de trabajo.

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
  - *Decisión:* Mantenimiento estricto de WebFlux en `:api` y Web MVC (Servlet) en `:user-auth`.
  - *Por qué:* Previene bloqueos de hilos Netty y conflictos de dependencias entre Tomcat y Netty.
- **DTOs Basados en Roles y Proyecciones:**
  - *Decisión:* Separación explícita entre DTOs de entrada/salida y DTOs según la jerarquía de roles (Admin, Usuario, etc.).
  - *Por qué:* Evita la sobreexposición de datos sensibles (campos como contraseñas hash, tokens o metadatos internos) y optimiza las consultas SQL.
- **Java 24 + Spring Boot 3.x:**
  - *Decisión:* Mantener compilación y toolchain en Java 24 aplicando banderas `--add-opens` en `JavaCompile`.
  - *Por qué:* Permite explorar las últimas características de Java mientras se otorga acceso por reflexión a Lombok y MapStruct sobre el AST.
- **Gestión de BD y Migraciones con Liquibase:**
  - *Decisión:* Control de esquemas PostgreSQL mediante changelogs YAML en `:user-auth`.
  - *Por qué:* Garantiza trazabilidad y reproducibilidad del esquema de BD en entornos locales y de CI/CD.
- **Lectura previa de `copilot-instructions.md`:**
  - *Decisión:* `AGENTS.md` ordena leer `.github/copilot-instructions.md` antes de cualquier revisión, ajuste o creación de código.
  - *Por qué:* La línea anterior (“alinearse en estilo y patrones”) no cubría tests, migraciones ni config (I/J/K); la nueva redacción lo hace exigible sin duplicar reglas.
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
  - *Aprendizaje:* Comparar IDs `Long` con `!=` compara referencias y da falsos negativos fuera del rango cacheado; usar siempre `equals` (detectado en `BaseDaoCatalogTranslationRules.java:35`, pendiente P-06).
- **Secuencias huérfanas en Liquibase:**
  - *Aprendizaje:* `autoIncrement: true` y `defaultValueSequenceNext` son excluyentes por columna; crear `createSequence` + `autoIncrement` deja la secuencia huérfana. El patrón correcto es el de `20260909_0001_create_languages_001.yaml` (pendiente P-02).

---

## 4. Próximos Pasos

1. **Entidad `Usuario` y Repositorios:** Finalizar el modelado de la entidad `Usuario`, sus enums/roles y la capa de persistencia con Spring Data JPA.
2. **Resolver pendientes de `pending.md`:** P-06 (comparación `!=` y criterio de borrado, nivel Alto) y P-07 (códigos i18n en `GlobalExceptionHandler`) son los de mayor prioridad técnica.
3. **Módulo de Seguridad (Spring Security):** Implementar la configuración de seguridad, gestión de tokens (JWT/OAuth2) y filtros de autenticación/autorización.
4. **Estandarización de Pruebas:** Definir las clases base y patrones para tests unitarios (Mockito/AssertJ/Instancio) y de integración (`@SpringBootTest` / `@DataJpaTest`).
5. **Optimización de DTOs y Mappers:** Diseñar la jerarquía de DTOs según el rol del usuario y mapeadores con MapStruct.
