---
applyTo: "**/src/main/resources/db/**, **/src/test/**/migration/**"
---

# Migraciones Liquibase

- Los servicios con persistencia administran el esquema con Liquibase; Hibernate valida el esquema, no lo genera. Cada servicio persistente mantiene su changelog raíz y sus migraciones en la estructura vigente del módulo.
- Sigue el patrón de changelog raíz, nombres de archivo, IDs, secuencias, auditoría y restricciones que ya use el módulo. No traslades convenciones de un servicio a otro sin comprobar su esquema real.
- Los cambios de esquema requieren plan y aprobación explícita antes de editar migraciones. No ejecutes dropAll, reconstrucciones ni comandos de base de datos sin aprobación específica para la base destino y el efecto.
- Consulta 1guides/03-liquibase-cli.md, 1guides/04-crear-migraciones.md y 1guides/09-plantilla-migracion.md. Para perfiles y reconstrucción local, consulta 1guides/10-mcp-postgres.md y verifica primero el perfil y destino.
