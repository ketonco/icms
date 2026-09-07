# Crear migraciones

El changelog raiz es `user-auth/src/main/resources/db/migration-root.yaml` y usa `includeAll` relativo:

```yaml
databaseChangeLog:
  - includeAll:
      path: migrations/
      relativeToChangelogFile: true
      errorIfMissingOrEmpty: true
```

Los changelogs individuales viven en `user-auth/src/main/resources/db/migrations/`. Usar nombres ordenables:

```text
YYYYMMDD_NNNN_descripcion.yaml
```

Ejemplo: `20260907_0001_create_revinfo_001.yaml`.

Cada changeset necesita un `id` estable y un `author`:

```yaml
databaseChangeLog:
  - changeSet:
      id: 20260907_0001_create_example_001
      author: system
      changes:
        - createTable:
            tableName: example
            columns:
              - column:
                  name: id
                  type: BIGINT
                  autoIncrement: true
                  constraints:
                    primaryKey: true
                    nullable: false
```

No editar un changeset ya aplicado en una base compartida; agregar otro changeset correctivo.

Aplicar y comprobar:

```powershell
.\gradlew.bat :user-auth:update
```

Despues revisar `databasechangelog` en PostgreSQL.
