---
description: Reconstruye la BD de un módulo (dropAll + update + seed) y valida esquema y datos con el MCP de Postgres
agent: build
---

Para el módulo o módulos indicados en $ARGUMENTS (nombre(s) o `all`; si está vacío, usa `all`):

1. Resuelve los módulos: `all` = todos los que apliquen
   `migration-conventions` (hoy solo `user-auth`). Un nombre que no la
   aplique: infórmalo y detente.
2. Precondiciones: verifica que PostgreSQL esté levantado (contenedor
   `postgreSQL_DB` en `docker ps`) y que el MCP `postgres` tenga sus
   herramientas cargadas. Si falta la BD, infórmalo y detente; si falta el
   MCP, indica reiniciar OpenCode y detente sin ejecutar nada.
3. Seguridad: revisa la URL efectiva de Liquibase en `<mod>/build.gradle`
   (bloque `liquibase`, incluida la propiedad `dbUrl` si se pasa por CLI) y
   confirma que apunte a `localhost`. Si no es local, NO ejecutes nada y
   detente.
4. Ejecuta en orden, mostrando cada comando antes de ejecutarlo y parando en
   el primer fallo con su error completo:
   `.\gradlew.bat :<mod>:dropAll`, luego `:mod:update`, luego
   `:mod:bootRun --args='--spring.profiles.active=task --seed=all'`.
   Omite el seed si el módulo no tiene `SeedManager`.
5. Valida con el MCP `postgres` usando el perfil `<nombre del módulo>`. Si
   el perfil no existe, pide crearlo con `connection add` y detente hasta tu
   orden:
   a. tablas, columnas y secuencias declaradas en `db/migrations/*.yaml`
      existen en `information_schema`.
   b. `databasechangelog` sin errores y con todos los changesets del
      `migration-root.yaml`.
   c. por cada clase `*DataSeed.java`, compara filas y columnas clave
      (code, name, translation, active) contra los datos reales.
6. Reporte final: tabla de checks ✅/❌ con la consulta y el resultado. Si
   todo está ✅, termina ahí. Si algo falló, propón la entrada para
   `pending.md` y espera tu sí antes de escribir.
7. Únicos archivos modificables: `pending.md` y solo con tu sí. Este comando
   no edita código, migraciones ni seeds.
