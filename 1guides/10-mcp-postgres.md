# MCP de Postgres y perfiles de base de datos

Este MCP conecta a los agentes (OpenCode y Codex) con las bases de datos
del proyecto para validar esquema y datos sin salir de la sesión. Cada
microservicio tendrá su propia base, por eso los perfiles son por módulo.

## 1. Cómo está montado

- **Servidor MCP:** `@microsoft/postgres-mcp` arrancado desde
  `opencode.json` en la raíz del repositorio. Ese archivo solo dice cómo
  lanzarlo (`npx -y @microsoft/postgres-mcp run`); no lleva credenciales
  y por eso se puede versionar.
- **Perfiles:** cada conexión vive en `~/.postgres-mcp/connections.yaml`
  (fuera del repositorio) con nombre, host, puerto, base y modo de acceso.
- **Contraseña:** se guarda en el keyring de Windows, nunca en archivos
  del proyecto ni en variables de entorno (regla de `AGENTS.md`).
- **Requisito:** Node.js 22 o superior
  (`winget install OpenJS.NodeJS.LTS`).

### OpenCode frente a Codex

- OpenCode lee `opencode.json` automáticamente al iniciar la sesión.
- Codex registra el mismo servidor con
  `codex mcp add postgres -- npx -y @microsoft/postgres-mcp run`.
  Los perfiles y el keyring son compartidos porque los usa el mismo MCP.

## 2. Crear un perfil para una base existente

```powershell
npx -y @microsoft/postgres-mcp connection add <perfil> "postgresql://postgres@localhost:25432/<base>" --access-mode ro
npx -y @microsoft/postgres-mcp connection set-password <perfil>
npx -y @microsoft/postgres-mcp connection list
```

- El `set-password` se ejecuta en una terminal propia: pide la contraseña
  a ocultas y la guarda en el keyring. No uses la opción `--password`:
  la clave quedaría en el historial de la terminal.
- `--access-mode ro` deja al MCP solo de consulta; `rw` permite
  escritura. Para validación siempre basta `ro`.
- **Convención de nombre:** el perfil se llama igual que el módulo
  (`user-auth` → base `ICMS_UA`).
- Si el perfil nuevo no aparece en la sesión, reinicia OpenCode para que
  el MCP lo recargue.
- **Nota Windows:** si PowerShell bloquea `npx.ps1` por política de
  ejecución, usa `npx.cmd` en su lugar.

## 3. Crear la base de un módulo nuevo

1. Crear la base en el contenedor `postgreSQL_DB`:

   ```powershell
   docker exec postgreSQL_DB psql -U postgres -c "CREATE DATABASE ICMS_NUEVO;"
   ```

2. Aplicar `migration-conventions` en el `build.gradle` del módulo y
   apuntar el bloque `liquibase` a
   `jdbc:postgresql://localhost:25432/<base>`.
3. Parametrizar el `application.yml` del módulo con su propia variable
   `DB_*_NAME`.
4. Crear el perfil MCP con el paso 2, usando el nombre del módulo.
5. La primera corrida de `/rebuild-db <modulo>` crea el esquema con las
   migraciones y valida contra el perfil nuevo.

## 4. Cómo usa el comando /rebuild-db los perfiles

`/rebuild-db <modulo|all>` recorre estos pasos:

1. Resuelve los módulos con `migration-conventions`.
2. Verifica precondiciones: contenedor `postgreSQL_DB` arriba y MCP con
   herramientas cargadas (si no, reiniciar OpenCode).
3. Revisa que la URL efectiva de Liquibase apunte a `localhost` antes de
   un `dropAll`.
4. Ejecuta `dropAll` → `update` → `bootRun --seed=all`.
5. Conecta al MCP con el perfil del mismo nombre que el módulo y valida:
   tablas y columnas de `db/migrations/*.yaml` en `information_schema`,
   `databasechangelog` sin fallos, y las filas de cada `*DataSeed.java`.
6. Muestra el reporte de checks; solo escribe `pending.md` si algo falló
   y el desarrollador lo autoriza.

## 5. Comandos de referencia

```powershell
npx -y @microsoft/postgres-mcp connection list
npx -y @microsoft/postgres-mcp connection add <perfil> "<url>" --access-mode ro
npx -y @microsoft/postgres-mcp connection set-password <perfil>
npx -y @microsoft/postgres-mcp connection remove <perfil>
```

`remove` elimina el perfil y su contraseña del keyring.

## 6. Reglas de seguridad

- Nunca agregar la contraseña al `connection add`: siempre por
  `set-password` en terminal propia.
- `opencode.json` y los perfiles del repositorio no contienen
  credenciales.
- El `dropAll` solo se ejecuta con URL de `localhost` (paso 3 del
  comando); nunca contra una base remota.
