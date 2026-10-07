---
description: Fase 2 del flujo de refactorización: ejecuta el plan aprobado y escribe executed.md
agent: build
---

Recibe en `$ARGUMENTS` el `<slug>` de una refactorización con
`.opencode/refactors/<slug>/plan.md` aprobado.

Pasos:

1. Precondición: lee `.opencode/refactors/<slug>/plan.md`. Si no existe o
   no está aprobado por mí, detente y repórtalo sin ejecutar nada.
2. Ejecuta los cambios paso a paso, respetando las convenciones del
   proyecto (`AGENTS.md`, `.github/copilot-instructions.md`).
3. Escribe `.opencode/refactors/<slug>/executed.md` con:
   - Qué se modificó (archivo:línea)
   - Qué quedó igual
   - Qué no se pudo hacer y por qué
4. Reglas:
   - Ejecuta DIRECTAMENTE en esta sesión. PROHIBIDO usar el tool
     `subagent` o delegar en `refactor-*`.
   - No te saltees pasos del plan ni hagas cambios fuera de su alcance.
   - No modifiques tests para hacerlos pasar.
   - TDD: si el plan incluye tests, escríbelos primero (en rojo) y
     después el código que los pone en verde.
   - Nunca des la tarea por hecha con tests en rojo.
   - Cero APIs deprecadas ni warnings pendientes en el código que
     escribas: usa la alternativa vigente (ej. `Locale.of`, nunca
     `new Locale`) y aplica `@SuppressWarnings("null")` en los tests
     siguiendo el patrón existente; nunca suprimas errores reales ni
     warnings de seguridad.
   - Si encuentras un problema que el plan no contempla, detente y
     repórtame en vez de improvisar.
   - Respeta markdownlint (blancos alrededor de encabezados y listas,
     niveles sin saltos) en todo `.md` que escribas.
5. Límites de edición: no toques `AGENTS.md`, `.github/**`, `MEMORY.md`,
   `pending.md`, `ROADMAP.md` ni `.opencode/agents/**`. Shell limitado a
   `gradlew` (compilar y tests); la aprobación del plan me autoriza esos
   comandos, cualquier otro shell pídeme permiso primero.
6. Señal de loop 1: si terminas detenido o con un "no se pudo:
   <motivo>", repórtalo en esa misma frase; será el contexto para que yo
   re-invogue `/refactor-plan <slug> --replan`.
7. Respuesta: archivos modificados (`archivo:línea`), resultado de los
   tests ejecutados y cualquier decisión que el plan no cubría.
8. Cierre — elige según el resultado y dame UNA sola línea copiable:
   - Si todo salió: `Ejecuta: /refactor-test <slug>`
   - Si terminaste detenido o con "no se pudo: <motivo>":
     `Ejecuta: /refactor-plan <slug> --replan "bloqueo: <motivo> +
     <archivo:línea>"`
