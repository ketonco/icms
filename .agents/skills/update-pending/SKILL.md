---
name: update-pending
description: Audita código contra las instrucciones del proyecto y propone hallazgos para pending.md. Úsala para revisar código, comparar con instrucciones y estándares, actualizar pendientes o hacer code review. Audit code against project instructions, propose findings for pending.md.
---

Ante el alcance indicado por el usuario en su mensaje (un módulo, un paquete o todo el proyecto):

1. Compara: extrae de `AGENTS.md`, `.github/copilot-instructions.md` y `MEMORY.md`
   las reglas aplicables al alcance y anótalas como checklist de revisión.
2. Determina el delta: revisa con prioridad lo cambiado desde la última revisión
   (`git status` / `git diff`) y anota el commit o alcance cubierto; lo ya
   cubierto sin cambios no se re-audita.
3. Revisa el código punto por punto contra ese checklist, con evidencia
   (`archivo:línea`); lo que cumpla se marca verificado, lo que no, hallazgo.
4. Presenta la lista de hallazgos y espera autorización explícita. Sin tu orden
   no se escribe nada en `pending.md` ni se marca ningún `TODO`.
5. Registra en `pending.md` con la plantilla estándar (P-XX consecutivo, sin
   duplicar) y marca cada línea a corregir con `TODO (pending P-XX)`.
6. Respeta markdownlint (blancos alrededor de encabezados/listas, niveles sin
   saltos) en todo `.md` que toques. Solo registra: no corrijas código.
7. Reconcilia lo existente: si detectas que un pendiente de `pending.md` ya está
   corregido en el código, elimínalo. Si a una entrada le falta un detalle
   (ubicación, evidencia, opción), actualízala en vez de duplicarla. Si el
   alcance no arroja hallazgos ni olvidos, no registres nada.
