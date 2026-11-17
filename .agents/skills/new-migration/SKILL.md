---
name: new-migration
description: Planifica una migración Liquibase desde una entidad JPA del monorepo ICMS. Úsala para tablas, auditoría o traducciones.
---

Para la entidad indicada: lee AGENTS.md y usa docs/contexto/project-map.md para seleccionar la instrucción de Liquibase. Consulta 1guides/04-crear-migraciones.md y 1guides/09-plantilla-migracion.md como procedimiento y ejemplo.

1. Localiza la clase y resuelve su jerarquía, incluidas las clases base de shared-kernel. Confirma que sea @Entity; si es @MappedSuperclass u otro tipo, detente.
2. Obtén nombre de tabla y columnas de anotaciones y código real. No inventes columnas por herencia sin verificar.
3. Identifica el módulo dueño y su ubicación real de migraciones. Si la clase está en shared-kernel, pregunta qué servicio y base de datos posee la tabla.
4. Presenta el plan, archivo y YAML propuesto, IDs, secuencias, constraints y validaciones. Espera aprobación explícita antes de crear o editar archivos.
5. No ejecutes migraciones ni comandos de base de datos. Selección de destino y acciones destructivas requieren autorización específica.
