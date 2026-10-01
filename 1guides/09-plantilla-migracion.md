# Plantilla de migración Liquibase (`user-auth`)

Guía copia/pega para crear un archivo de migración nuevo. Todo ejemplo sale del
patrón real de `20260909_0001_create_languages.yaml`.

## 1. Nombre del archivo y `id`

Formato: `YYYYMMDD_NNNN_descripcion.yaml`

- Fecha + secuencia del día (4 dígitos) + descripción en `snake_case`.
- Sin segundo `_NNN` final; el orden lo da el `NNNN` tras la fecha.
- El `id` del **primer** changeset es `<ARCHIVO>_001` (`<ARCHIVO>` sin extensión);
  los siguientes changesets del mismo archivo añaden sufijo
  (`<ARCHIVO>_indexes_002`, `<ARCHIVO>_revision_003`, `<ARCHIVO>_revision_indexes_004`).
- Nunca editar ni renombrar un changeset ya ejecutado en una BD compartida.
  En BD local de desarrollo se edita el archivo directamente.

## 2. Qué changesets lleva cada caso

| Caso | Changesets |
| --- | --- |
| Tabla de entidad (auditable) | `_001` tabla+secuencia, `_002` índices, `_003` tabla `_aud`, `_004` índices `_aud` |
| Tabla relacional (N–M, ej. `user_types`) | `_001` tabla + PK compuesta + FKs + índice. **Sin secuencia y sin `_aud`** |

## 3. Por qué existe la tabla `_aud` (Envers)

Los repositorios heredan de `BaseRepository`, que extiende
`RevisionRepository` (Spring Data Envers). En cada `insert`/`update`/`delete`,
Envers escribe una fila en `<tabla>_aud` con la revisión (`rev` → `revinfo`)
y el tipo (`revtype`: 0 crear, 1 actualizar, 2 borrar). Sin la tabla `_aud`,
la aplicación falla al persistir.

Las tablas relacionales no son entidades y Envers no las audita: **no llevan
`_aud`** (ver `20260916_0004_create_user_types.yaml`).

## 4. Esqueleto: tabla de entidad (copiar y adaptar)

```yaml
databaseChangeLog:
  - changeSet:
      id: <ARCHIVO>_001
      comment: "Create <tabla> table"
      author: "<autor>"
      changes:
        - createSequence:
            sequenceName: <tabla>_seq
            startValue: 1
            incrementBy: 50

        - createTable:
            tableName: <tabla>
            columns:
              - column:
                  name: id
                  type: BIGINT
                  defaultValueSequenceNext: <tabla>_seq
                  constraints:
                    primaryKey: true
                    primaryKeyName: pk_<tabla>
                    nullable: false
              - column:
                  name: code
                  type: VARCHAR(50)
                  constraints:
                    nullable: false
                    unique: true
              - column:
                  name: name
                  type: VARCHAR(50)
                  constraints:
                    nullable: false
                    unique: true
              - column:
                  name: active
                  type: BOOLEAN
                  constraints:
                    nullable: false
              # created_at / created_by / updated_at / updated_by (auditoría)

      rollback:
        - dropTable:
            cascadeConstraints: true
            tableName: <tabla>

  - changeSet:
      id: <ARCHIVO>_indexes_002
      author: "<autor>"
      preConditions:
        - onFail: HALT
        - tableExists:
            tableName: <tabla>
      changes:
        - createIndex:
            tableName: <tabla>
            indexName: idx_id_<tabla>
            columns:
              - column:
                  name: id
        - createIndex:
            tableName: <tabla>
            indexName: idx_code_<tabla>
            columns:
              - column:
                  name: code

      rollback:
        - dropIndex:
            tableName: <tabla>
            indexName: idx_id_<tabla>
        - dropIndex:
            tableName: <tabla>
            indexName: idx_code_<tabla>

  - changeSet:
      id: <ARCHIVO>_revision_003
      author: "<autor>"
      changes:
        - createTable:
            tableName: <tabla>_aud
            columns:
              # Mismas columnas de <tabla>, todas con nullable: false,
              # más estas dos al final:
              - column:
                  name: rev
                  type: INTEGER
                  constraints:
                    nullable: false
              - column:
                  name: revtype
                  type: TINYINT
                  constraints:
                    nullable: false

        - addPrimaryKey:
            tableName: <tabla>_aud
            columnNames: id, rev
            constraintName: pk_<tabla>_aud

        - addForeignKeyConstraint:
            baseTableName: <tabla>_aud
            baseColumnNames: rev
            referencedTableName: revinfo
            referencedColumnNames: rev
            constraintName: fk_<tabla>_aud_revinfo
            onDelete: CASCADE

      rollback:
        - dropTable:
            cascadeConstraints: true
            tableName: <tabla>_aud

  - changeSet:
      id: <ARCHIVO>_revision_indexes_004
      author: "<autor>"
      preConditions:
        - onFail: HALT
        - tableExists:
            tableName: <tabla>_aud
      changes:
        - createIndex:
            tableName: <tabla>_aud
            indexName: idx_<tabla>_aud_rev
            columns:
              - column:
                  name: rev
        - createIndex:
            tableName: <tabla>_aud
            indexName: idx_<tabla>_aud_id
            columns:
              - column:
                  name: id

      rollback:
        - dropIndex:
            tableName: <tabla>_aud
            indexName: idx_<tabla>_aud_rev
        - dropIndex:
            tableName: <tabla>_aud
            indexName: idx_<tabla>_aud_id
```

## 5. Esqueleto: tabla relacional (copiar y adaptar)

```yaml
databaseChangeLog:
  - changeSet:
      id: <ARCHIVO>_001
      author: <autor>
      changes:
        - createTable:
            tableName: <tabla_relacional>
            columns:
              - column:
                  name: <entidad_a>_id
                  type: <UUID|BIGINT>
                  constraints:
                    nullable: false
              - column:
                  name: <entidad_b>_id
                  type: BIGINT
                  constraints:
                    nullable: false

        - addPrimaryKey:
            tableName: <tabla_relacional>
            columnNames: <entidad_a>_id, <entidad_b>_id
            constraintName: pk_<tabla_relacional>

        - addForeignKeyConstraint:
            baseTableName: <tabla_relacional>
            baseColumnNames: <entidad_a>_id
            referencedTableName: <entidad_a>
            referencedColumnNames: id
            constraintName: fk_<tabla_relacional>_<entidad_a>
            onDelete: CASCADE

        - addForeignKeyConstraint:
            baseTableName: <tabla_relacional>
            baseColumnNames: <entidad_b>_id
            referencedTableName: <entidad_b>
            referencedColumnNames: id
            constraintName: fk_<tabla_relacional>_<entidad_b>
            onDelete: RESTRICT

      rollback:
        - dropTable:
            tableName: <tabla_relacional>
            cascadeConstraints: true

  - changeSet:
      id: <ARCHIVO>_index_002
      author: <autor>
      changes:
        - createIndex:
            indexName: idx_<tabla_relacional>_<entidad_b>_id
            tableName: <tabla_relacional>
            columns:
              - column:
                  name: <entidad_b>_id
      rollback:
        - dropIndex:
            indexName: idx_<tabla_relacional>_<entidad_b>_id
            tableName: <tabla_relacional>
```

## 6. Checklist antes de darla por lista

- [ ] `id` con `defaultValueSequenceNext: <tabla>_seq` (nunca `autoIncrement`
  junto a `createSequence`; ver P-02 en `pending.md`).
- [ ] `code` único, `name` único, `active` no nulo en catálogos.
- [ ] Cada changeset con su `rollback`.
- [ ] Changesets de índices con `preConditions` (`tableExists`).
- [ ] `_aud` con PK `(id, rev)` + FK a `revinfo` (`CASCADE`) + sus 2 índices.
- [ ] Tablas relacionales sin secuencia y sin `_aud`.
- [ ] `revinfo` existe como primer changeset del proyecto
  (`20260907_0001_create_revinfo.yaml`).
