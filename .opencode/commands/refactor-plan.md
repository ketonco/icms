---
description: Fase 1 del flujo de refactorización: inspecciona el código y escribe o reescribe el plan en .opencode/refactors/<slug>/plan.md
agent: build
---

Recibe en `$ARGUMENTS` el alcance de la refactorización. Formatos aceptados:

- `<alcance en palabras>` — primera planificación.
- `<slug> --replan "bloqueo: <motivo> + <archivo:línea>"` — loop 1,
  tras detenerse el executor.
- `<slug> --replan "FAIL: <test>, <mensaje>, <archivo:línea>"` — loop 2,
  tras veredicto FAIL en los tests.

Pasos:

1. Lee `AGENTS.md` y `.github/copilot-instructions.md` para conocer las
   convenciones del proyecto. Inspecciona el código actual relacionado con
   el alcance recibido.
2. Si es `--replan`: antes de ajustar, lee el plan anterior en
   `.opencode/refactors/<slug>/plan.md` y el contexto del fallo o bloqueo
   indicado. Reescribe el plan en esa misma ruta.
3. Formula hasta 5 preguntas de clarificación si algo no está claro.
   Preséntamelas y espera mis respuestas antes de escribir el plan.
4. Con las respuestas, escribe el plan en
   `.opencode/refactors/<slug>/plan.md` con:
   - Objetivo de la refactorización
   - Archivos afectados (con archivo:línea)
   - Cambios paso a paso
   - Riesgos y cómo mitigarlos
   - Tests que deben ejecutarse para validar
5. En el encabezado del plan anota `Ciclo N`: `Ciclo 1` en la primera
   versión, y suma 1 por cada `--replan`.
6. Reglas:
   - Ejecuta DIRECTAMENTE en esta sesión. PROHIBIDO usar el tool
     `subagent` o delegar en `refactor-*`.
   - Propone el `<slug>` en kebab-case (ej. `dto-validation-i18n`).
   - No modifiques código: solo escribes el plan. Únicos archivos
     modificables: `.opencode/refactors/**`.
   - Respeta markdownlint (blancos alrededor de encabezados y listas,
     niveles sin saltos) en todo `.md` que escribas.
7. Detente y espera mi aprobación explícita del plan: no se ejecuta nada
   sin ella. Si me pides un cambio sobre un plan ya aprobado, reescribe
   el plan, muéstrame el diff y espera mi sí de nuevo antes de continuar.
8. Límites de ciclo: máximo 2 `--replan` por bloqueo de ejecución
   (loop 1) y 3 ciclos totales por FAIL de tests (loop 2). Agotado el
   límite, detente y escálame el estado alcanzado con los fallos
   restantes.
9. Respuesta: las rutas de los archivos creados o modificados y un
   resumen de 5 líneas como máximo (o la lista de preguntas si las hay).
10. Cierre (solo tras mi aprobación explícita del plan): `Plan
    aprobado. Ejecuta: /refactor-exec <slug>` — esa es mi señal para
    copiar el comando de la fase siguiente.
