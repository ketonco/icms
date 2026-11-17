---
description: Ejecuta el plan ICMS de refactorización aprobado
agent: build
---

Recibe el slug: $ARGUMENTS

Lee el plan persistido y verifica que la aprobación cubra rutas y comandos. Si no existe el plan, no está aprobado o el alcance no es verificable, detente. Sigue el plan exactamente; antes de ejecutar cualquier comando, confirma que está expresamente incluido y autorizado conforme a AGENTS.md. No edites pending.md, MEMORY.md, fuentes canónicas o perfiles. Informa archivos modificados, qué quedó igual, verificaciones ejecutadas/no ejecutadas y bloqueos. Si la aprobación incluyó executed.md, escribe allí el reporte. Ante cambios de alcance, pausa y presenta una propuesta nueva.
