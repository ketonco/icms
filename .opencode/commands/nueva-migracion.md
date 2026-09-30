---
description: Planifica un archivo de migración Liquibase desde una clase entidad
agent: plan
---

Para la clase entidad indicada en $ARGUMENTS:

1. Localiza la clase (acepta nombre simple o calificado) y resuelve su jerarquía
   de herencia (`extends`, `@MappedSuperclass` de `shared-kernel`) y verifica que
   sea `@Entity`. Si es `@MappedSuperclass` o no es entidad, no hay tabla que
   migrar: infórmalo y detente.
2. Toma el nombre de la tabla de `@Table(name = "...")`. Si la entidad no tiene
   `@Table` con `name` indicado, detente e informa que es necesario para crear
   la migración.
3. Deriva las columnas de la herencia: `LongAuditableEntity`/`UUIDAuditableEntity`
   (`id` + auditoría), `BaseCatalogEntity` (`code`, `active`, `name`),
   `BaseCatalogTranslationEntity` (`catalog_id`, `language_id`, `translation`,
   `description`). Sin `@Entity` propia no hay migración.
4. Lee `1guides/09-plantilla-migracion.md` (esqueleto `_001` a `_004`, `_aud`
   Envers, regla de relacionales) y la sección J de
   `.github/copilot-instructions.md` (nombres, `id`, secuencias).
5. Detecta el módulo dueño por el paquete (`com.icms.<modulo>...`) y usa sus
   `src/main/resources/db/migrations/` (cada microservicio tiene su BD propia).
   Si la entidad vive en `shared-kernel`, pregunta en qué BD va antes de asumir.
6. Presenta el plan y el YAML propuesto y espera autorización explícita para
   crearlo. En modo plan no se ejecuta nada sin tu orden.
