---
name: qa-review
description: QA de ICMS: revisa arquitectura, código y pruebas según las instrucciones canónicas y presenta hallazgos sin modificar registros automáticamente.
---

# Revisión QA de ICMS

1. Lee AGENTS.md y MEMORY.md. Usa docs/contexto/project-map.md para seleccionar solo las instrucciones técnicas y guías pertinentes al alcance. Inspecciona código, configuración y pruebas como evidencia del estado actual.
2. Delimita el alcance. Revisa diff o historial solo cuando las herramientas y la autorización lo permitan; no ejecutes comandos sin autorización explícita conforme a AGENTS.md. No presupongas ramas, commits o archivos de historial que no existan.
3. Revisa convenciones aplicables al módulo, incluidas separación MVC/WebFlux, dependencias, persistencia, migraciones, configuración, i18n y pruebas. Aplica cobertura selectiva; no exijas duplicar tests de abstracciones genéricas ya cubiertas.
4. Para cada hallazgo informa ubicación, evidencia, impacto, severidad, recomendación y opciones. Distingue hechos de inferencias. Propón casos de prueba para validar correcciones; no declares ejecutadas pruebas que no corriste.
5. Lee pending.md solo si es pertinente. No agregues, edites ni elimines entradas. Presenta posibles altas/cierres al usuario y espera autorización específica para escribir. No agregues marcadores TODO automáticamente.
6. Trata MEMORY.md como contexto temporal, no como lista de tareas. No lo actualices rutinariamente. Propón cambios por separado y espera autorización.
7. No edites código, pruebas, instrucciones canónicas ni registros durante una revisión. Si el usuario pide correcciones, presenta un plan separado para aprobación.

## Entrega

Entrega resumen, hallazgos priorizados, evidencia, pruebas sugeridas o ejecutadas, decisiones pendientes y limitaciones. No actualices histórico QA ni otro archivo salvo autorización específica. Respeta markdownlint en Markdown autorizado.
