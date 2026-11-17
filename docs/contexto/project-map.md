# Mapa del proyecto ICMS

Este documento permite localizar el módulo, las instrucciones y las guías relevantes para una tarea. No reemplaza las fuentes enlazadas ni repite sus reglas.

## Fuentes de instrucciones

| Necesidad | Fuente canónica | Guía de apoyo |
| --- | --- | --- |
| Reglas de trabajo, permisos, memoria y pendientes | [AGENTS.md](../../AGENTS.md) | — |
| Índice técnico para Copilot | [copilot-instructions.md](../../.github/copilot-instructions.md) | — |
| Convenciones comunes de código | [project-conventions.instructions.md](../../.github/instructions/project-conventions.instructions.md) | [Estructura](../../1guides/01-estructura-proyecto.md) |
| Gateway reactivo | [api-webflux.instructions.md](../../.github/instructions/api-webflux.instructions.md) | [API Gateway](../../1guides/06-api-gateway.md), [tests](../../1guides/10-gateway-tests.md) |
| Autenticación MVC/JPA | [user-auth-mvc-jpa.instructions.md](../../.github/instructions/user-auth-mvc-jpa.instructions.md) | [Estructura](../../1guides/01-estructura-proyecto.md), [migraciones](../../1guides/04-crear-migraciones.md) |
| Código compartido | [shared-kernel.instructions.md](../../.github/instructions/shared-kernel.instructions.md) | [Estructura](../../1guides/01-estructura-proyecto.md) |
| Pruebas | [testing.instructions.md](../../.github/instructions/testing.instructions.md) | [Pruebas del monorepo](../../1guides/11-pruebas.md) |
| Gradle y dependencias | [gradle-dependencies.instructions.md](../../.github/instructions/gradle-dependencies.instructions.md) | [Java y Gradle](../../1guides/02-java-y-gradle.md), [dependencias](../../1guides/08-dependencias.md) |
| Esquema de base de datos | [liquibase.instructions.md](../../.github/instructions/liquibase.instructions.md) | [CLI](../../1guides/03-liquibase-cli.md), [crear migraciones](../../1guides/04-crear-migraciones.md), [plantilla](../../1guides/09-plantilla-migracion.md) |
| Auditoría dinámica de seguridad | [Política común](../../.agents/skills/qa-app-security/SKILL.md), [Codex](../../.agents/skills/codex-qa-app-security/SKILL.md), [OpenCode](../../.opencode/skills/opencode-qa-app-security/SKILL.md) | [Plantilla de informe](../../.agents/skills/qa-app-security/references/security-pending-template.md) |

## Módulos actuales

| Módulo | Responsabilidad | Ubicación | Instrucciones |
| --- | --- | --- | --- |
| api | Gateway HTTP reactivo sin persistencia propia | [api/](../../api/) | [WebFlux](../../.github/instructions/api-webflux.instructions.md) |
| user-auth | Usuarios, autenticación y seguridad con MVC/JPA | [user-auth/](../../user-auth/) | [MVC/JPA](../../.github/instructions/user-auth-mvc-jpa.instructions.md) |
| shared-kernel | Biblioteca común de entidades base, DTOs, mappers y excepciones | [shared-kernel/](../../shared-kernel/) | [Shared kernel](../../.github/instructions/shared-kernel.instructions.md) |

## Selección rápida

1. Lee AGENTS.md y MEMORY.md al iniciar una tarea.
2. Identifica los archivos y módulos que realmente tocaría la tarea.
3. Lee las instrucciones que apliquen a esas rutas y solo las guías enlazadas que resuelvan el procedimiento concreto.
4. Inspecciona código, configuración y pruebas de esos módulos para distinguir diseño esperado de estado implementado.
5. Skills y agentes deben indicar qué instrucciones y guías requieren; no deben pedir la lectura completa de documentación no relacionada.

Los módulos mencionados como futuros en documentación o planes no se consideran existentes hasta que aparezcan en la estructura y configuración del monorepo.
