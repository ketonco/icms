---
description: Orquesta una refactorización vía los 3 subagentes (flujo coordinador desde build, solo para comparar contra /refactor-plan|exec|test directos)
agent: build
---

Recibe en `$ARGUMENTS` el alcance de la refactorización. Este comando es
la réplica de `.opencode/agents/refactor-coordinator.md` disparada desde
build: en este flujo SÍ se usan los subagentes. Usa `.opencode/refactors/`
para los mismos archivos de handoff (`plan.md`, `executed.md`,
`test-result.md`).

Pasos:

1. Lanza `refactor-planner` con el alcance. Pásale en el prompt: la fase
   en que está, la petición original con mis palabras y decisiones, y las
   rutas de archivo relevantes. Si el planner responde con preguntas,
   muéstramelas, espera mis respuestas y relánzalo. Repite hasta que
   exista `.opencode/refactors/<slug>/plan.md`.
2. Muéstrame el plan y espera mi aprobación explícita. No continúes sin
   ella.
3. Con el plan aprobado, lanza `refactor-executor` con la ruta del plan.
   - Si escribe `executed.md`: continúa al paso 4.
   - Si se detiene con un problema: relanza `refactor-planner` con el
     contexto del problema y repite desde el paso 2. Máximo 2 re-planes;
     si el problema persiste, escálame.
4. Lanza `refactor-tester` con las rutas del plan y del executed.
5. Lee `test-result.md` y muéstrame el veredicto.
   - Si PASS: sugiere ejecutar `qa-review`.
   - Si FAIL: relanza `refactor-planner` con el contexto del fallo
     (test, mensaje, archivo:línea) y repite desde el paso 2. Máximo 3
     ciclos; agotados, detente y escálame con el estado alcanzado y los
     fallos restantes.
6. En cada llamada a un subagente pásale todo lo que necesita (no ven
   esta conversación): fase y esperativa, petición original literal,
   rutas de archivo, resultado de la fase anterior.
7. Cambios de requisitos: si pido un cambio sobre un plan ya aprobado,
   primero el planner actualiza `plan.md` y muestra el diff; con mi sí
   se ejecuta. Nunca ejecutar un plan modificado sin aprobación.
8. Informa en una línea al empezar cada fase.
