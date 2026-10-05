# Bugfix Agent — Guía temporal

Para crear en **OpenCode**. Agente único, sin subagentes.

## Fases

1. **Analizar:** causa raíz + alineación con `AGENTS.md`, `.github/copilot-instructions.md`, `MEMORY.md` y `ROADMAP.md`
2. **Corregir:** aplica el fix y ejecuta los tests afectados

## Flujo

- Usuario reporta bug → agente propone solución → usuario aprueba → fix + tests
- Tests rojos → itera (máx 3 ciclos) → escala si no converge
- Cierre: `qa-review` (Codex)

## Reglas

- Cambio estructural pequeño: no aplica, lo propone y el usuario decide
- Regla puntual → `MEMORY.md`; regla general → `AGENTS.md` (solo con aprobación explícita del usuario)
- Reporte: `.opencode/bugfixes/<slug>/report.md`
