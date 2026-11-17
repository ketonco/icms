---
description: Reconstruye y valida la base local de un módulo ICMS cuando el usuario aprueba expresamente destino y efecto
agent: build
---

Este comando puede borrar y reconstruir datos. No lo ejecutes por una petición ambigua.

1. Identifica el módulo solicitado y confirma que usa las convenciones Liquibase. No aceptes all por defecto.
2. Antes de cualquier comando, inspecciona de forma permitida la configuración para identificar URL, perfil y base destino. Presenta módulo, host, base, perfil y efecto exacto de dropAll, update y seed. Si no puedes verificar que es una base local desechable, detente.
3. Espera aprobación explícita que nombre el destino y autorice la operación destructiva. La aprobación general del plan no cubre un destino o efecto no descrito.
4. Comprueba que PostgreSQL y las herramientas MCP requeridas estén disponibles. No ejecutes nada si falta una precondición.
5. Tras la aprobación, ejecuta solo los comandos exactos aprobados, en orden y deteniéndote ante el primer fallo. Usa el Wrapper de Gradle y nombres de tareas/perfiles comprobados en el módulo.
6. Valida esquema y seeds con el perfil del módulo si está disponible y autorizado. Informa consultas y resultados sin exponer secretos.
7. Reporta checks, errores y limitaciones. Si encuentras hallazgos, propón entradas para pending.md; no escribas ni elimines entradas sin aprobación específica.

Este comando no modifica código, migraciones, seeds, pending.md ni MEMORY.md.
