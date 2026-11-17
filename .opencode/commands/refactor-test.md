---
description: Ejecuta únicamente las verificaciones aprobadas para una refactorización ICMS
agent: build
---

Recibe el slug: $ARGUMENTS

Lee el plan aprobado y el reporte de ejecución. Ejecuta solo pruebas/comandos incluidos en el alcance aprobado y permitidos por AGENTS.md. No modifiques código ni pruebas. Si se autorizó el reporte, escribe .opencode/refactors/<slug>/test-result.md con comandos exactos, resultados, fallos, verificaciones omitidas y veredicto PASS/FAIL. Un fallo es FAIL; no lo debilites ni lo omitas. No declares como ejecutado lo que no corriste.
