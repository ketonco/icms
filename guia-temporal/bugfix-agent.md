# Bugfix Agent — Guía temporal

Creado en **OpenCode** como agente nativo en `.opencode/agents/bugfix-agent.md`.

## Estructura

- Agente único, `mode: primary`, sin subagentes
- Permisos: `edit` en código (con deny de protegidos) + `shell` para `*gradlew*`

## Flujo

1. Preguntas (máx 5) → usuario responde
2. Replicar bug → documenta en `report.md`
3. Analizar → hipótesis clara + alineación con convenciones
4. Proponer solución → usuario aprueba
5. Corregir → TDD si aplica + tests + verificación de regresión
6. Reportar → `.opencode/bugfixes/<slug>/report.md`

## Decisiones cerradas

- No downgrades como solución fácil; verificar versiones más nuevas primero
- Rama `bugfix*` verificada al inicio; no cambia sin aprobación
- Worktrees para trabajo paralelo en ramas distintas
- Bug de código del desarrollador → `pending.md` (aprendizaje)
- Bug externo → flujo normal de bugfix
- `detected-bug.md` para bugs encontrados durante el trabajo
- MCP de PostgreSQL si el bug involucra datos
- Puede escribir tests unitarios/de integración para validar el fix
- Loop: máx 3 ciclos si tests fallan
- Cierre: `qa-review` (Codex)
