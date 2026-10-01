# AGENTS.md - Plataforma de Microservicios ICMS

ICMS es una plataforma backend empresarial de microservicios construida con Java 24 y Spring Boot 4.1.1. Proporciona autenticación de usuarios, gestión de roles y permisos, catálogo de productos con mini-inventario, búsqueda avanzada de ítems, módulo de facturación con portales para administradores y usuarios, un carrito de compras de prueba para simulación, y la futura integración de IA y n8n para comercio conversacional vía WhatsApp.

Fuente de verdad: `.github/copilot-instructions.md` prevalece sobre el código real, que a su vez prevalece sobre este `AGENTS.md`, que prevalece sobre `MEMORY.md`. Lo ya implementado en el esqueleto es ley: no se reescribe código funcional para ajustar documentación.

## Stack y estructura

- **Java y Spring:** Java 24 puro sin código fuente Kotlin y Spring Boot 4.1.1 con paquetes Jakarta EE obligatorios.
- **Arquitectura Monorepo:**
  - `:api`: Spring Cloud Gateway con WebFlux reactivo sobre Netty. No tiene persistencia propia, solo enruta a servicios downstream por HTTP no bloqueante. Prohibido Tomcat, MVC o JPA bloqueante en este módulo.
  - `:user-auth`: Microservicio de autenticación, usuarios y seguridad con Spring Web MVC y Spring Data JPA.
  - `:shared-kernel`: Entidades base, DTOs, mappers MapStruct y manejo base de excepciones.
- **Sistema de construcción:** Monorepo Gradle con catálogo centralizado `gradle/libs.versions.toml` y plugins de convención Kotlin DSL en `buildSrc/src/main/kotlin/*.gradle.kts`.
- **Automatización e IA futura:**
  - **Orquestación:** n8n Community Edition auto-hospedado con Docker.
  - **Motor de IA:** Spring AI con Ollama local Llama 3 o DeepSeek, o APIs gratuitas Google Gemini o Groq API.
  - **Mensajería:** WhatsApp Business Cloud API o Evolution API con túnel local ngrok o Cloudflare Tunnel.
  - **Pagos:** Stripe en modo Sandbox.
- **Nota de entorno:** si el entorno local usa Java 25, Gradle falla con `IllegalArgumentException: 25` en `JavaVersion.parse`. El estándar del proyecto sigue siendo Java 24. Esa excepción es temporal y no cambia la versión oficial.

## Comandos

- **Compilar y construir:** `./gradlew build` en Linux o macOS, `gradlew.bat build` en Windows.
- **Ejecutar pruebas:** `./gradlew test` o `gradlew.bat test`.
- **Ejecutar un módulo:** `./gradlew :user-auth:bootRun` o `gradlew.bat :user-auth:bootRun`.
- **Inspección de dependencias:** `./gradlew dependencyInsight --dependency <libreria>` o `gradlew.bat dependencyInsight --dependency <libreria>`.

## Convenciones

- **Idioma de código:** todo el código, identificadores y Javadoc en inglés.
- **Idioma de documentación:** guías Markdown, tutoriales y explicaciones al desarrollador en español.
- **Markdown sin warnings:** todo `.md` cumple markdownlint v0.41.1 con cero warnings, con líneas en blanco alrededor de encabezados y listas y sin saltos de nivel de encabezado.
- **Nombres:** `camelCase` para variables y métodos, `PascalCase` para clases, `SNAKE_CASE` para constantes.
- **Javadoc:** todo método no trivial incluye propósito, `@param`, `@throws` y `@return`.
- **Directrices de Copilot:** antes de revisar o crear código, leer `.github/copilot-instructions.md` y alinearse a sus convenciones de estilo, patrones, tests, migraciones y configuración.

## Reglas de dominio y trampas conocidas

- **Aislamiento WebFlux contra MVC:** nunca importar `spring-boot-starter-web` en `:api` ni WebFlux reactivo en `src/main` de módulos Servlet como `:user-auth`. En `src/test` se permite `spring-boot-starter-webflux` solo como cliente `WebTestClient`.
- **Persistencia reactiva:** R2DBC o conectores no bloqueantes solo aplican si un módulo reactivo necesitara base de datos. El gateway actual no necesita R2DBC porque no persiste.
- **Paquetes:** usar siempre `jakarta.*` para JPA, validación y servlets. Nunca usar `javax.*`.
- **Mapeadores:** usar MapStruct y Lombok para entidades y DTOs. No escribir getters, setters o builders manuales.

## Forma de trabajar

- **Planificar primero:** explicar solución propuesta, desglose paso a paso y archivos afectados antes de pedir autorización.
- **Mostrar cada cambio en el chat:** cada cambio incluye su contenido exacto en bloque de código o diff con `archivo:línea`, indicando qué se agregó, qué se quitó y qué quedó igual, antes de darlo por finalizado.
- **Comandos y guías:** el nombre de archivo de comandos canónicos en `.opencode/commands/` va en inglés como `/new-migration`. La carpeta `commands/` en raíz es legado. El cuerpo de comandos y las guías en `1guides/` van en español.
- **Inspección sin autorización:** solo lectura con `read`, `glob` y `grep` está siempre permitida sin pedir permiso.
- **Acciones con autorización:** `shell`, `edit` y `write` siempre requieren plan previo y autorización explícita del desarrollador.
- **Gestión de calidad con `pending.md`:**
  - **Nombre canónico:** `pending.md` en minúsculas. El duplicado `PENDING.md` en mayúsculas es histórico y debe eliminarse con Git por el desarrollador.
  - **Auditoría y registro:** tras cada revisión o generación de código, auditar y registrar fallos, deuda técnica o pruebas faltantes en `pending.md` con la plantilla estándar.
  - **Auto-actualización limitada:** por defecto se pide autorización para tocar `pending.md` y `MEMORY.md`. Solo se tocan sin pregunta extra cuando la tarea, skill o comando en curso lo ordena explícitamente.
  - **Auto-eliminación al validar:** solo en tarea de validación, si se verifica en código que un punto ya está corregido y probado, se elimina esa entrada de `pending.md`.
  - **Estructura para hallazgos:** cada ítem incluye identificador `P-XX`, ubicación exacta `archivo:línea`, problema, contexto, opciones con recomendación y nivel de acción.
  - **Protocolo de dependencias:** para nueva librería o cambio en `gradle/libs.versions.toml`:
    1. Explicar justificación técnica.
    2. Dar al menos dos opciones viables.
    3. Esperar aprobación antes de modificar el catálogo.
- **Verificación de pruebas:** cada cambio de código indica las pruebas unitarias y de integración necesarias para validarlo.

## Limites

- **Siempre:** sugerir código limpio, dar formato según el espacio de trabajo y usar inspección de solo lectura.
- **Pregunta antes:**
  - Cada modificación, creación o eliminación de archivos.
  - Agregar o cambiar dependencias en `gradle/libs.versions.toml`.
  - Cambios de esquema o migraciones Liquibase.
  - Ejecutar comandos de terminal o construcción.
- **Nunca:**
  - NUNCA ejecutar comandos, alterar archivos o correr pruebas sin autorización explícita previa, salvo la inspección de solo lectura y la auto-actualización limitada ya descritas.
  - NUNCA hacer `git commit`, `git stash` o `git push`, reservados al desarrollador.
  - NUNCA escribir contraseñas o tokens en archivos versionados que no sean de ejemplo.

## Verificacion

- Describir los casos exactos con Mockito, AssertJ o `WebTestClient` y `RestAssured` que validan la funcionalidad antes de darla por finalizada.

## Memoria

- Al empezar, leer `MEMORY.md` para conocer estado y decisiones.
- Al terminar una tarea, actualizarlo solo si la tarea lo pide o hay decisión nueva con su porqué y errores a evitar.
- Mantenerlo breve con máximo 250 líneas: resumir o eliminar lo que no aporte.
- Si algo se vuelve regla permanente, proponer moverlo a `AGENTS.md`.
- Nunca guardar datos sensibles como claves o tokens.

## Uso de la plantilla para `pending.md`

Usa esta plantilla por cada hallazgo real en `pending.md`. Duplica el bloque, reemplaza corchetes y asigna identificador único consecutivo como `P-01` o `P-02`. No dejes marcadores sin completar.

## P-XX - Titulo descriptivo del hallazgo

**Donde y TODO:** `[modulo]/[ruta_al_archivo]:[linea]` y estado `agregado` o `no agregado` con su línea.

**Problema:** [Descripción clara de la inconsistencia o violación].

**Contexto y explicación:** [Por qué sucede y cómo afecta a backend, hilos o arquitectura].

**Opciones estándar:**

- A) [Opción 1 recomendada]
- B) [Opción 2 alternativa]

**Recomendación:** [Acción concreta propuesta].

**Nivel de acción requerido:** [Alto, Medio o Bajo con justificación breve].
