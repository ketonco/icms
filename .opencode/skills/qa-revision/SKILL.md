---
name: Revisión QA de ICMS
description: Revisa cambios, código, arquitectura e instrucciones de ICMS y presenta hallazgos sin modificar registros automáticamente.
---

# Revisión QA de ICMS

## Inicio y alcance

- Lee AGENTS.md y MEMORY.md. Usa docs/contexto/project-map.md para escoger las instrucciones técnicas y guías aplicables al módulo o rutas solicitadas.
- Revisa solo el alcance indicado y evidencia actual de código/configuración/pruebas. No presupongas frontend, módulos, trackers, ramas o guías que no existan.
- Lee pending.md solo si es pertinente. No agregues, edites ni elimines hallazgos. Las propuestas de registro se entregan al usuario para aprobación específica.
- No ejecutes comandos, builds, pruebas ni servicios sin autorización explícita conforme a AGENTS.md.

## Pasadas

### Pasada 1 — Cobertura y completitud

Comprueba la lógica y contratos afectados, cambios incluidos/excluidos, pruebas pertinentes y documentación tocada. Propón los casos faltantes con rutas y aserciones esperadas.

### Pasada 2 — Coherencia

Busca contradicciones, duplicación, supuestos no verificados y referencias obsoletas. Aplica instrucciones específicas del módulo y verifica que el diseño no se confunda con el estado implementado.

### Pasada 3 — Replicabilidad

Comprueba que el cambio se entienda desde las fuentes pertinentes, que referencias señaladas existan y que cada verificación reportada realmente se haya ejecutado. Distingue lo no comprobado.

## Severidades

- Crítica: seguridad, pérdida de datos o ruptura total.
- Alta: función esencial rota o contrato violado.
- Media: error en casos relevantes o deuda con impacto.
- Baja: defecto menor verificable.
- Mejora: propuesta opcional sin defecto actual.

## Reporte y límites

Por hallazgo incluye severidad justificada, ruta:línea cuando sea posible, evidencia, impacto y recomendación. Distingue hechos, inferencias y mejoras. No fabriques hallazgos para completar el informe.

Presenta las posibles altas, cierres o modificaciones de pending.md como propuestas; no alteres el archivo sin autorización explícita específica. No actualices MEMORY.md ni un histórico de QA salvo que estén incluidos y autorizados en el alcance. No corrijas código ni pruebas durante la revisión; para implementar correcciones, prepara un plan separado. Agrupa preguntas en rondas de hasta cinco. Commits, push, merge y despliegues están reservados al usuario.
