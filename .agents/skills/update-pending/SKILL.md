---
name: update-pending
description: Review source code against project instructions and current standards, then register findings in pending.md. Use for code audits, instruction compliance checks, or tech-debt reviews.
---

Ante el alcance indicado por el usuario en su mensaje (un módulo, un paquete o todo el proyecto):

1. Lee `AGENTS.md`, `.github/copilot-instructions.md`, `MEMORY.md` y `pending.md`
   (entradas existentes y última numeración P-XX usada).
2. Revisa el código del alcance contra las instrucciones y los estándares
   vigentes (Jakarta, Lombok/MapStruct, Envers, aislamiento MVC/WebFlux, tests,
   migraciones, i18n). Verifica cada hallazgo en el código real, no supongas.
3. Registra cada hallazgo real en `pending.md` con la plantilla estándar:
   identificador P-XX consecutivo, ubicación exacta (`archivo:línea`), problema,
   contexto, opciones, recomendación y nivel de acción. No dupliques entradas
   ya existentes.
4. Agrega un comentario `TODO (pending P-XX)` en cada archivo y línea a
   corregir, para que aparezcan en el panel de pendientes del IDE.
5. Respeta markdownlint (blancos alrededor de encabezados/listas, niveles sin
   saltos) en todo `.md` que toques.
6. Solo registra: no corrijas código ni documentación. Las correcciones las hace
   el equipo en sesiones de código.
7. Reconcilia lo existente: si detectas que un pendiente de `pending.md` ya está
   corregido en el código, elimínalo. Si a una entrada le falta un detalle
   (ubicación, evidencia, opción), actualízala en vez de duplicarla.
