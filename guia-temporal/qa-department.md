# QA Department — Guía temporal

Para crear en **Codex (GPT)**. Reemplaza `qa-review` como puerta final pre-producción.

## Ramas

`developer` → `qa` → `master`. El agente nunca fusiona; el usuario decide.

## Veredictos

- **BLOCK:** tests rojos, build roto, seguridad, datos corruptos → no sube
- **PASS WITH MINORS:** deuda menor registrada en `pending.md` → puede subir
- **PASS:** puede subir

## Subagentes (5)

- `linter`: markdownlint, formato, warnings de build
- `copilot-instructions-validator`: vs `.github/copilot-instructions.md` y `AGENTS.md`
- `security-validator`: secretos, dependencias, patrones inseguros
- `functional-validator`: lógica de negocio, tests faltantes/débiles (base: `qa-review` actual)
- `test-runner`: ejecuta el suite real

## Flujo

1. Fusionar `developer` → `qa`
2. Correr departamento → reporte en `.opencode/qa/<slug>/report.md`
3. Veredicto → usuario fusiona a `master` si procede

## Pendiente al crear

- [ ] Definir catálogo "peligroso" vs "menor"
- [ ] Absorber `qa-review` como base del `functional-validator`
- [ ] Retirar la skill `qa-review` cuando el departamento esté estable
