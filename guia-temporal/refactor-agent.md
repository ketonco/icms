# Refactor Agent — Guía temporal

Para crear en **OpenCode**. Orquestador + 3 subagentes.

## Estructura

- `refactor-planner`: revisa código actual, máx 5 preguntas, escribe plan en `.opencode/refactors/<slug>/plan.md`
- `refactor-executor`: lee plan, ejecuta, escribe en `.opencode/refactors/<slug>/executed.md`
- `refactor-tester`: ejecuta tests específicos, veredicto en `.opencode/refactors/<slug>/test-result.md`

## Flujo

1. Planner pregunta (máx 5) → usuario responde
2. Planner escribe plan → executor ejecuta → tester valida
3. Tests verdes → avisa para `qa-review` (Codex) como cierre
4. Tests rojos → loop con planner (máx 3 ciclos, luego escala al usuario)

## Reglas

- Tester nunca debilita tests para hacerlos pasar
- `<slug>` kebab-case decidido por el usuario
- `qa-review` siempre es el cierre final

## Caso inicial: i18n de validaciones DTO

- Anotaciones Jakarta con mensajes inline → `MessageSource` + properties
- Pendiente definir: convención de claves, idiomas (en-US, es-ES), fallback

## Pendiente al crear

- [ ] Skill o comando orquestador
- [ ] 3 subagentes (o modos de una skill)
- [ ] Probar con el caso i18n de DTO
