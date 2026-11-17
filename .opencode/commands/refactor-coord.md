---
description: Coordina el flujo de refactorización ICMS con los perfiles planner, executor y tester
agent: build
---

Alcance: $ARGUMENTS

1. Invoca refactor-planner con la solicitud, decisiones y rutas pertinentes. Debe consultar AGENTS.md, docs/contexto/project-map.md y solo instrucciones aplicables.
2. Presenta el plan en el chat y espera aprobación explícita. No persistas plan.md antes de que se apruebe la ruta.
3. Con aprobación, vuelve a invocar refactor-planner para que persista el plan aprobado en .opencode/refactors/<slug>/plan.md si esa ruta fue autorizada. Confirma que existe y luego invoca refactor-executor con fase/rutas aprobadas.
4. Si hay bloqueo o cambio de alcance, consulta al usuario; no continúes bajo la aprobación previa.
5. Invoca refactor-tester solo para pruebas y comandos aprobados. Guarda el reporte si fue incluido en el alcance.
6. Presenta cambios, comandos y resultados, limitaciones y decisiones pendientes. Los subagentes no conocen esta conversación; pasa en cada llamada petición original, decisiones, rutas, fase y resultado anterior.
