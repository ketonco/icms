---
name: Pruebas UX-UI del frontend ICMS
description: Prueba la interfaz web de ICMS por alcance solicitado. Si todavía no hay frontend implementado, detecta la ausencia y reporta que no hay UI disponible; cuando se agregue, descubre stack, rutas y ejecución desde los archivos reales del proyecto.
---

# Pruebas UX-UI del frontend ICMS

## Propósito

Usa esta skill cuando el usuario solicite probar visual o funcionalmente una interfaz frontend de ICMS. Permanece activa para el futuro frontend, pero no asume que ya existe. No la uses para implementar frontend, probar solo el backend o ejecutar una prueba integral no delimitada.

## Fase 0 — Detección sin efectos

1. Lee AGENTS.md, MEMORY.md y docs/contexto/project-map.md. Inspecciona el árbol y manifiestos para localizar una aplicación frontend real.
2. Si no hay frontend implementado, informa que no existe una interfaz que probar y detente. No levantes servicios, ejecutes comandos, crees artifacts de prueba ni registres pendientes.
3. Si existe, identifica su ruta, framework, scripts, dependencias, configuración de host/puerto, backend requerido y guía técnica. No uses direcciones o comandos heredados de otros proyectos.
4. Antes de iniciar servicios, ejecutar comandos o usar navegador sobre la aplicación, presenta el destino, acciones, datos sintéticos, riesgos, alcance, criterios de parada y artefactos a escribir. Espera aprobación explícita conforme a AGENTS.md.

## Fase 1 — Plan

Tras autorización para planificar, crea un plan detallado solo si la ruta de salida está aprobada. El plan enumera objetivo, precondiciones, rutas, host comprobado, rol/credenciales requeridas (nunca incluyas secretos), pasos, datos de prueba únicos, resultados esperados, evidencia, criterios pasa/falla y limpieza. Si la UI requiere autenticación, pide al usuario introducir credenciales directamente en la interfaz.

## Fase 2 — Ejecución

Ejecuta únicamente pasos aprobados contra el origen autorizado. Usa Playwright u otra herramienta disponible si está configurada. Por paso registra observación y evidencia en la ruta aprobada. No accedas a producción o servicios compartidos, no cambies datos persistentes y no fuerces controles de acceso. Si un paso requiere acción no aprobada, detente. Marca pasos bloqueados y no inventes atajos.

## Fase 3 — Informe

Entrega resultado por paso, evidencia, errores de consola/red relevantes, responsive/teclado si estaban en alcance, datos creados o modificados, limpieza realizada o pendiente, severidad, hallazgos confirmados, limitaciones y pasos no ejecutados. No registres hallazgos en pending.md ni crees un tracker frontend por iniciativa propia; presenta propuestas al usuario para decisión posterior.

## Artefactos y seguridad

Guarda plan, conclusión y capturas solo en una ruta de salida aprobada, por ejemplo .opencode/frontend-tests/<slug>-<fecha>/. No sobrescribas corridas existentes. No incluyas secretos, cookies, tokens, credenciales, PII ni capturas sin revisar. Los comandos, inicio de servicios, uso activo del navegador y escrituras requieren aprobación conforme a AGENTS.md. No hagas commits, push, merge o despliegues.

## Referencias

Usa docs/contexto/project-map.md para encontrar el frontend y las fuentes canónicas una vez que existan. Esta skill es la fuente operativa de pruebas UX; no duplica instrucciones generales o reglas técnicas del futuro frontend.
