# Refactor Agent — Guía temporal

Creado en **OpenCode** como agentes nativos en `.opencode/agents/`.

## Archivos

- `refactor-coordinator.md` — mode: primary, orquesta las 3 fases
- `refactor-planner.md` — mode: subagent, inspecciona y escribe el plan
- `refactor-executor.md` — mode: subagent, ejecuta el plan
- `refactor-tester.md` — mode: subagent, ejecuta tests y emite veredicto

## Flujo

1. Planner propone `<slug>` y hasta 5 preguntas → usuario responde
2. Planner escribe `.opencode/refactors/<slug>/plan.md` → usuario aprueba
3. Executor ejecuta → `.opencode/refactors/<slug>/executed.md`
4. Tester valida → `.opencode/refactors/<slug>/test-result.md`
5. Tests verdes → avisa para `qa-review` (Codex)
6. Tests rojos → loop con planner (máx 3 ciclos, luego escala)

## Decisiones cerradas

- Tester: tests de la refactor + suite completo si toca código compartido
- Slug: lo propone el planner, se confirma con el usuario
- Gate: el usuario aprueba el plan antes de ejecutar
- Tester nunca debilita tests
- Sin APIs deprecadas en código nuevo (`Locale.of`, no `new Locale`)
- Tests de DTO vía `Validator` inyectado, no vía controlador (copilot §I)
- `@SuppressWarnings("null")` en tests nuevos (patrón existente del proyecto)
- Todo `.md` generado cumple markdownlint (mismo criterio que bugfix-agent)

## Caso inicial: i18n de validaciones DTO

- Anotaciones Jakarta con mensajes inline → `MessageSource` + properties
- Pendiente definir: convención de claves, idiomas (en-US, es-ES), fallback
