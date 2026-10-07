---
description: Fase 3 del flujo de refactorización: ejecuta los tests y emite veredicto PASS/FAIL en test-result.md
agent: build
---

Recibe en `$ARGUMENTS` el `<slug>` de una refactorización con
`.opencode/refactors/<slug>/executed.md`.

Pasos:

1. Lee `.opencode/refactors/<slug>/plan.md` y
   `.opencode/refactors/<slug>/executed.md`.
2. Ejecuta los tests específicos de lo refactorizado. Si la refactor toca
   código compartido (`shared-kernel`, clases base), ejecuta el suite
   completo del módulo afectado.
3. Escribe `.opencode/refactors/<slug>/test-result.md` con:
   - Tests ejecutados y su resultado
   - Veredicto final: PASS o FAIL
   - Si FAIL: lista numerada con `archivo:línea`, qué incumple (test o
     parte del plan) y qué se espera
   - Sugerencias que no incumplan el plan van aparte, en "Opcional", y no
     bloquean
4. Reglas:
   - Ejecuta DIRECTAMENTE en esta sesión. PROHIBIDO usar el tool
     `subagent` o delegar en `refactor-*`.
   - Nunca modifiques código ni tests: solo ejecutas y reportas. Únicos
     archivos modificables: `.opencode/refactors/**`.
   - Un test en rojo es un fallo real: no lo debilites ni lo saltes.
   - Si un test falla por razones ajenas a la refactor (fallo
     preexistente), repórtalo como tal en el veredicto.
   - Shell limitado a `gradlew` para compilar y correr tests.
   - Respeta markdownlint (blancos alrededor de encabezados y listas,
     niveles sin saltos) en todo `.md` que escribas.
5. Veredicto PASS: infórmame y sugiere ejecutar `qa-review`.
6. Veredicto FAIL: esa lista numerada es mi señal de loop 2; entrégala
   con el formato `FAIL: <test>, <mensaje>, <archivo:línea>` para que yo
   re-invoque `/refactor-plan <slug> --replan`.
7. Respuesta: rutas leídas, tests ejecutados con su resultado y veredicto
   final PASS o FAIL.
8. Cierre — elige según el veredicto y dame UNA sola línea copiable:
   - Si PASS: veredicto final + `Todo en verde, puedes ejecutar
     qa-review.`
   - Si FAIL: `Ejecuta: /refactor-plan <slug> --replan "FAIL: <test>,
     <mensaje>, <archivo:línea>"`
