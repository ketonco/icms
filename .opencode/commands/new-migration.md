---
description: Planifica una migración Liquibase a partir de una entidad del monorepo ICMS
agent: plan
---

Para la clase entidad indicada en $ARGUMENTS:

1. Lee AGENTS.md y docs/contexto/project-map.md. Consulta la instrucción de Liquibase y la guía 1guides/09-plantilla-migracion.md.
2. Localiza la clase y comprueba que sea @Entity; resuelve su herencia real, incluidos los tipos base de shared-kernel. Si no es entidad, detente e informa.
3. Obtén el nombre de tabla y columnas desde las anotaciones y clases base efectivas. No inventes columnas a partir de una plantilla.
4. Identifica el módulo dueño por paquete, dependencias y configuración real. Si pertenece a shared-kernel, pregunta qué servicio y base de datos deben poseer la tabla.
5. Presenta cambios propuestos, nombre y contenido YAML, secuencias/constraints y pruebas o validaciones necesarias. Espera autorización explícita antes de crear o cambiar archivos.
6. No ejecutes migraciones ni comandos de base de datos. Las operaciones destructivas y la selección de base destino requieren autorización específica.
