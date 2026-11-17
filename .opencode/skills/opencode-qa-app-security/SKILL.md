---
name: opencode-qa-app-security
description: Coordina en OpenCode una auditoría dinámica autorizada de las APIs locales de ICMS y, cuando exista, su interfaz web.
metadata:
  short-description: Auditoría dinámica de seguridad de ICMS en localhost
  opencode/slash: "true"
---

# Auditoría de seguridad de ICMS en OpenCode

Usa esta skill para auditorías dinámicas solicitadas por el usuario. Carga qa-app-security como política común. El alcance puede incluir APIs locales de api/user-auth y la interfaz de ICMS cuando exista. No asumas URLs, puertos, rutas, servicios o frontend implementado.

## Flujo coordinado

1. **Plan:** invoca qa-security-planner con el alcance y la información que dio el usuario. El planificador no usa herramientas ni inspecciona archivos. Presenta origen, rutas/endpoints, acciones, cuentas/datos sintéticos, efectos, límites, riesgos y criterios de parada. No propongas escribir en pending.md ni en un tracker. Espera aprobación explícita.
2. **Ejecución:** tras la aprobación, invoca qa-security-auditor con el plan. Ejecuta solo comprobaciones enumeradas contra el origen exacto aprobado. Cada herramienta activa mantiene su propio control de aprobación. Si la aplicación o herramienta no está disponible, informa la limitación; no la instales ni levantes servicios sin autorización.
3. **Informe:** pasa evidencia redactada a qa-security-reporter para validar su clasificación y reproducibilidad. El reportero devuelve el informe al coordinador y no edita archivos. Entrega el resultado en la conversación. Si el usuario solicita un archivo, acuerda y autoriza la ruta antes de escribirlo.

## Límites de OpenCode

- Auditoría de caja negra: no inspecciones código fuente, archivos ejecutables, configuración privada ni secretos.
- El auditor no usa read, glob, grep, list, edit ni shell. Puede usar solo herramientas de navegador/ZAP autorizadas por el perfil y aprobadas para cada operación.
- Antes de cada llamada ZAP con destino, valida esquema, host y puerto contra el plan. Limita tráfico a localhost y rutas aprobadas; detente ante redirecciones fuera de alcance.
- No uses funciones ZAP que escriban archivos o reportes. El coordinador entrega resultados en chat; cualquier escritura requiere aprobación específica y ruta explícita.
- No cambies perfiles, configuración global ni instalación de MCP durante una auditoría.
- No incluyas credenciales, tokens, cookies, PII, query strings sensibles ni capturas sin redactar. No hagas fuerza bruta, DoS, extracción, persistencia ni operaciones destructivas.

Los perfiles qa-security-planner, qa-security-auditor y qa-security-reporter aplican estos límites. Verifica que reporter no tenga permisos de lectura/escritura sobre un tracker y que el auditor siga aislado de archivos y shell.
