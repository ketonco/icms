# QA Department — Guía temporal

Creado en **Codex (GPT)** como skill orquestadora + subagentes TOML.

## Archivos

- `.agents/skills/qa-department/SKILL.md` — orquestador (skill Codex)
- `.codex/agents/qa-linter.toml` — markdownlint, formato, warnings build
- `.codex/agents/qa-instructions.toml` — vs copilot-instructions, AGENTS, MEMORY
- `.codex/agents/qa-security.toml` — secretos, dependencias, patrones inseguros
- `.codex/agents/qa-functional.toml` — absorbe qa-review: lógica, tests §I, pending (incluidos ignorados), MCP postgres
- `.codex/agents/qa-test-runner.toml` — ejecuta el suite real

## Flujo

1. Verifica rama `qa` + alcance (qa_historic.md) + pending.md incluidos ignorados
2. Lanza 5 subagentes en paralelo → hallazgos por texto
3. Consolida `.opencode/qa/<slug>/report.md` sin duplicados
4. Veredicto: BLOCK / PASS WITH MINORS / PASS
5. BLOCK: pending crítico vigente (ignorado o detectado) en sección destacada ⚠
6. Tras tu sí: escribe solo pending.md, qa_historic.md (formato intacto), MEMORY.md
7. Nunca fusiona a master — decides tú. Máx 2 vueltas tras BLOCK

## Decisiones cerradas

- Reporte escrito por el orquestador (opción A)
- Sandbox: read-only ×4, workspace-write para test-runner
- MCP postgres para integridad; si necesita otro, lo indica en el reporte
- qa_historic.md intocado (solo fecha+commit)
- qa-review se queda para revisiones sencillas
- AGENTS.md / copilot-instructions jamás se editan
