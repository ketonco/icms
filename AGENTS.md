# AGENTS.md - Plataforma de Microservicios ICMS

ICMS es una plataforma backend empresarial de microservicios construida con Java 24 y Spring Boot 3.x (cite: 7, 9). Proporciona autenticación de usuarios, gestión de roles y permisos, catálogo de productos con mini-inventario, búsqueda avanzada de ítems, módulo de facturación con portales para administradores y usuarios, un carrito de compras de prueba para simulación, y la futura integración de IA y n8n para comercio conversacional vía WhatsApp (cite: 7, 8).

## Stack y estructura

- **Java & Spring:** Java 24 (Java puro sin código fuente Kotlin) (cite: 9) y Spring Boot 3.x (uso estrictamente obligatorio de paquetes Jakarta EE) (cite: 9).
- **Arquitectura Monorepo:**
  - `:api`: Spring Cloud Gateway con WebFlux reactivo (Netty) (cite: 6, 9). Prohibido de forma estricta el uso de Tomcat/MVC o repositorios JPA bloqueantes en este módulo (cite: 9).
  - `:user-auth`: Microservicio de autenticación, gestión de usuarios y seguridad utilizando Spring Web (MVC) y Spring Data JPA (cite: 6, 9).
  - `:shared-kernel`: Entidades de dominio compartidas, DTOs, mappers y manejo base de excepciones (cite: 6, 9).

- **Sistema de Construcción:** Construcción multimódulo con Gradle (DSL Groovy/Kotlin) con catálogo centralizado `gradle/libs.versions.toml` y plugins de convención en `buildSrc/` (cite: 6, 7, 9).
- **Automatización e Integración con IA (Hoja de ruta futura):**
  - **Orquestación:** n8n Community Edition (Auto-hospedado mediante Docker).
  - **Motor de IA:** Spring AI integrado con Ollama (modelos locales Llama 3/DeepSeek) o APIs de capa gratuita (Google Gemini / Groq API) (cite: 8).
  - **Mensajería:** Número de prueba de WhatsApp Business Cloud API / Evolution API conectado mediante túnel local (ngrok / Cloudflare Tunnel) (cite: 8).
  - **Pruebas de Pago:** Modo de prueba de Stripe (Sandbox) (cite: 8).

## Comandos

- **Compilar y Construir:** `./gradlew build` (cite: 7)
- **Ejecutar Pruebas Unitarias:** `./gradlew test`[cite: 7, 9]
- **Ejecutar un Módulo Específico:** `./gradlew :user-auth:bootRun`[cite: 7]
- **Inspección de Dependencias:** `./gradlew dependencyInsight --dependency <librería>`[cite: 7]

## Convenciones

- **Idioma del Código y Comentarios:** Todo el código, parámetros de métodos, variables, clases y comentarios Javadoc DEBEN estar escritos en inglés (cite: 7, 9).
- **Guías de Desarrollo y Documentación:** Todas las guías en formato Markdown, tutoriales y explicaciones creadas para el desarrollador DEBEN estar escritas en ESPAÑOL[cite: 8].
- **Convenciones de Nombres:** Estricto `camelCase` para variables y métodos, `PascalCase` para clases y `SNAKE_CASE` para constantes[cite: 7].
- **Estándar Javadoc:** Todo método no trivial debe incluir Javadoc que describa su propósito, `@param`, `@throws` y tipos de retorno `@return`[cite: 7].
- **Directrices de Copilot:** Antes de cualquier revisión, ajuste o creación de código, leer `.github/copilot-instructions.md` y alinearse estrictamente a sus convenciones vigentes (estilo, patrones, tests, migraciones y configuración)[cite: 7, 9].

## Reglas de dominio / trampas conocidas

- **Aislamiento WebFlux vs MVC:** Nunca importar `spring-boot-starter-web` en `:api` ni componentes reactivos de WebFlux en módulos basados en Servlets (`:user-auth`) (cite: 9).
- **Importación de Paquetes:** Usar SIEMPRE paquetes `jakarta.*` para clases de JPA, Validación y Servlets[cite: 9]. NUNCA sugerir importaciones de `javax.*`[cite: 9].
- **Mapeadores de Datos y Entidades:** Usar anotaciones de MapStruct y Lombok para mapeos entre entidades y DTOs[cite: 9]. NO generar código repetitivo (boilerplate) de getters/setters o patrones builder manualmente[cite: 9].

## Forma de trabajar

- **Planificar Primero:** Explicar siempre la solución propuesta, el desglose paso a paso y los archivos afectados ANTES de solicitar autorización (cite: 7).
- **Mostrar Cada Cambio en el chat:** Cada cambio que se vaya a realizar o se esté realizando debe mostrarse en el chat con su contenido exacto (bloque de código o diff con `archivo:línea`), no solo describirlo. Indicar qué se agregó, qué se quitó y qué quedó igual, antes de darlo por finalizado.
- **Comandos Personalizados en Inglés:** Los nombres de archivo de comandos en `.opencode/commands/` deben estar en inglés (ej. `/new-migration`). El cuerpo del comando y las guías (`1guides/`) se mantienen en español.
- **Gestión Automatizada de Calidad (`PENDING.md`):**
  - **Auditoría y Registro:** Tras cada revisión o generación de código, el agente debe auditar el estado del proyecto. Si detecta fallos, deudas técnicas o pruebas faltantes, debe registrarlos en `PENDING.md` siguiendo la plantilla estándar de reporte.
  - **Auto-Eliminación al Validar:** Cuando se le pida validar el proyecto o un punto específico, si el agente verifica que la corrección ya fue aplicada y probada en el código fuente, **debe eliminar automáticamente dicha entrada de `PENDING.md`** para mantener el archivo limpio únicamente con los pendientes reales activos.
  - **Estructura Estándar para Nuevos Hallazgos:** Cada nuevo ítem en `PENDING.md` debe incluir: Código identificador (P-XX), Ubicación exacta (`archivo:línea`), Explicación del problema, Opciones/Recomendación técnica y Nivel de acción requerido[cite: 8, 9].
  - **Protocolo para Propuestas de Dependencias:** Si se requiere una nueva librería o modificar una dependencias:
  1. Explicar la justificación técnica[cite: 7].
  2. Proporcionar al menos dos opciones/alternativas viables[cite: 7].
  3. Esperar la aprobación antes de modificar `gradle/libs.versions.toml`[cite: 7, 9].
- **Requisitos de Verificación de Pruebas:** Cada vez que se proponga un cambio de código, indicar claramente las pruebas unitarias y de integración necesarias para validar la funcionalidad[cite: 9].

## Límites

- 🟢 **Siempre:** Sugerir código limpio, dar formato según las reglas del espacio de trabajo, ejecutar inspección local y mantener actualizado el archivo `PENDING.md` (registrando nuevos hallazgos o **eliminando automáticamente** los ya corregidos tras la validación) [cite: 7, 8].
- ⚠️ **Pregunta antes:**
  - CADA modificación de código, creación de archivos o eliminación de archivos[cite: 7].
  - Agregar o cambiar dependencias en `gradle/libs.versions.toml`[cite: 7, 9].
  - Cambios en el esquema de base de datos o migraciones de Liquibase[cite: 9].
  - Ejecutar comandos de terminal o de construcción[cite: 7].
- 🚫 **Nunca:**
  - NUNCA ejecutar comandos, alterar archivos o correr pruebas sin autorización explícita previa[cite: 7].
  - NUNCA realizar commits, stash o pushes de código (`git commit`, `git stash`, `git push` están reservados exclusivamente para el desarrollador)[cite: 7].
  - NUNCA escribir contraseñas, claves secretas o tokens de forma explícita (hardcoded) en archivos de configuración que no sean de ejemplo[cite: 6].
  ✅ Siempre: actualizar `MEMORY.md` al terminar cada tarea.

## Verificación

- Describir los casos de prueba exactos (por ejemplo, pruebas unitarias con Mockito, aserciones con AssertJ o pruebas de integración con WebTestClient) que deben ejecutarse para probar el correcto funcionamiento de la característica antes de darla por finalizada (cite: 9).

## Memoria

- Al empezar, lee `MEMORY.md` para conocer el estado del proyecto y las decisiones
tomadas.
- Al terminar una tarea, actualízalo: estado actual, decisiones importantes (con su
porqué) y errores a evitar.
- Mantenlo breve (máximo ~250 líneas): resume o elimina lo que ya no aporte.
- Si algo se convierte en una regla permanente, propón moverlo a `AGENTS.md` en lugar de
dejarlo en la memoria.
- No guardes nunca datos sensibles (claves, tokens, datos personales).

## Uso de la plantilla para `PENDING.md`

Usa la siguiente plantilla para cada hallazgo real que se registre en `PENDING.md`. Duplica el bloque por cada pendiente, reemplaza todos los textos entre corchetes con información concreta y asigna un identificador único y consecutivo (`P-01`, `P-02`, etc.). Incluye el nivel de acción requerido y no dejes marcadores sin completar.

## P-XX — [Título descriptivo del hallazgo]

**Dónde:** `[módulo]/[ruta_al_archivo]:[línea]`

**Ubicacion del TODO: agregado** `[módulo]/[ruta_al_archivo]:[línea]`

**Problema:** [Descripción clara de la inconsistencia, vulnerabilidad o violación de convención].

**Contexto / Explicación:** [Por qué sucede y cómo afecta al backend, hilos o arquitectura].

**Opciones estándar:**

- A) [Opción 1 - Recomendada]
- B) [Opción 2 - Alternativa]

**Recomendación:** [Acción concreta propuesta por la IA].

**Nivel de acción requerido:** [Alto / Medio / Bajo — justificación breve].
