# Informe de QA — 2026-10-09

## Alcance

- Rama: `qa`.
- Corte anterior: `ff82b07` (`qa_historic.md`, 2026-10-05).
- HEAD revisado: `45a361a`.
- Delta: commits posteriores al corte histórico y el cambio local en
  `.agents/skills/qa-department/SKILL.md`.
- Arquitectura: revisión completa de módulos, Gradle, separación MVC/WebFlux,
  seguridad, persistencia, pruebas, i18n y documentación.
- Reconciliación: se leyó todo `pending.md`; contiene P-15 y P-29.

## Veredicto: BLOCK

P-15 sigue clasificado como Alto y el servicio permite operaciones de escritura
anónimas en recursos de catálogo. La skill `qa-department` considera bloqueante
cualquier pendiente activo de nivel Alto. El resultado de tests y build fue
verde, pero no elimina estos riesgos de seguridad.

## Validador: instrucciones y arquitectura

- **P-15 continúa vigente.** `user-auth/src/main/java/com/icms/user_auth/config/SecurityConfig.java:39-50`
  exige autenticación para rutas no públicas, deshabilita form login y HTTP
  Basic, y no se encontró un mecanismo de autenticación configurado en
  `user-auth/src/main`.
- La misma configuración permite anónimamente `/api/v1/auth/languages/**` y
  `/api/v1/auth/user-status-translations/**`. Sus controladores implementan
  operaciones de escritura/actualización/eliminación además de lectura
  (`LanguageController.java:18-29`,
  `UserStatusTranslationController.java:18-38`). Esto permite modificar
  catálogos sin autenticación.
- La separación arquitectónica revisada sigue siendo Gateway WebFlux en `api`,
  MVC/JPA en `user-auth` y librería común en `shared-kernel`. No se encontró
  WebFlux en producción de módulos Servlet.
- La centralización de picocli y del BOM de Spring Boot ya está aplicada. La
  dependencia WebFlux de tests de `user-auth` está comentada; queda un marcador
  TODO P-26 obsoleto en `user-auth/build.gradle:24`.
- El delta modifica `.github/copilot-instructions.md` y `AGENTS.md` en commits
  previos al corte actual, pero no son cambios locales de esta corrida. No hay
  evidencia en el alcance revisado de que fueran cambios sin autorización.

## Validador: seguridad

- **Riesgo alto de autorización:** las rutas de idiomas y traducciones de
  estados incluyen endpoints mutables y están bajo `permitAll`. Restringir las
  escrituras a una identidad/autoridad adecuada y conservar públicas solo las
  lecturas que el producto requiera.
- **Driver PostgreSQL:** `gradle/libs.versions.toml:9` fija `42.7.4`, afectado
  por CVE-2026-54291 hasta `42.7.11`. La explotación requiere
  `channelBinding=require` y condiciones de intermediario TLS; el advisory
  fija la corrección en `42.7.12`. No se confirmó que la configuración actual
  use ese modo. Recomiendo actualizar a `42.7.12` o posterior.
  [Advisory GHSA-j92g-9f8w-j867](https://github.com/advisories/GHSA-j92g-9f8w-j867).
- `api/docker-compose.yml:25-26` publica `user-auth` en un puerto del host,
  permitiendo acceso directo al downstream en entornos donde ese Compose se
  use como despliegue. Para preproducción/producción, mantenerlo en red interna
  o limitar la exposición a loopback en desarrollo.
- No se encontraron secretos versionados. `api/.env` está ignorado; solo
  `.env.example` está versionado. No se observó concatenación SQL ni
  serialización de la contraseña después de su codificación.
- **Opcional:** el Gateway permite cualquier origen, método y encabezado CORS
  en `api/src/main/resources/application.yml:15-23`; no se encontró
  `allowCredentials`. Restringirlo a los orígenes necesarios para el despliegue.
- No se ejecutó un escáner automatizado de dependencias; el análisis de CVE se
  limitó a la versión directa de PostgreSQL JDBC y a las versiones gestionadas
  por Spring Boot.

## Validador: funcionalidad y pendientes

- **P-29 está resuelto y su entrada es obsoleta.** Los bundles actuales no
  contienen las quince claves denunciadas en `pending.md:37-60`; sus claves de
  negocio presentes están referenciadas en código de producción. Además,
  `MessagesBundleCoverageTest` pasó en la ejecución forzada de la suite. Se
  propone retirar P-29 de `pending.md`.
- **P-25 debe reabrirse o registrarse de nuevo.**
  `LanguageDataSeed.java:23-25` declara `en-US`, `es-ES` y `fr-FR` activos,
  pero `UserStatusTranslationDataSeed.java:61-129` solo carga traducciones
  inglesas y españolas para ACT, INA, SUS, DEL y PEN. Falta francés. El TODO
  P-25 existe en código, pero no en el pending actual. El MCP PostgreSQL no
  está disponible, por lo que no se verificaron las filas desplegadas.
- **P-23 está incompleto.** `UserController.java:24-31` devuelve HTTP 201 y
  `RestResponse.status=201`, pero no añade `Location`. `UserControllerIT.java:56-60`
  comprueba HTTP 201 y algunos campos, pero no el status del cuerpo ni
  `Location`. Queda un TODO P-23 huérfano en `RestResponse.java:37`.
- **P-27: aserciones insuficientes en los casos de DTO inválido.**
  `UserControllerIT.java:76-132` verifica HTTP 400 y un mensaje genérico, pero
  no el mapa `errors` con el campo que falló ni que no se haya persistido el
  usuario.
- **Respuesta 204 con cuerpo:** `shared-kernel/src/main/java/com/icms/shared/controller/ReadController.java:20-24,30-34`
  construye respuestas HTTP 204 con `RestResponse.noContent()`. Un 204 no
  transporta cuerpo, por lo que el wrapper y el mensaje G-002 no serán
  observables por el cliente. No se encontró test que fije ese contrato.
- Hay marcadores TODO huérfanos P-03 y P-26 en código/build, además de P-23 y
  P-25. P-03 está implementado mediante el catálogo; P-26 está comentado. Se
  propone limpiar esos marcadores en una tarea de código separada.

## Validador: tests

- `gradlew.bat test --rerun-tasks`: **BUILD SUCCESSFUL**, `22 actionable tasks`,
  todas ejecutadas. Pasaron `api:test`, `shared-kernel:test` y `user-auth:test`,
  incluidos los tests de integración que corren dentro de la tarea `test`.
- La prueba `MessagesBundleCoverageTest` pasó, consistente con que P-29 ya no
  corresponde a las claves actuales.
- `gradlew.bat build` también terminó correctamente en la revisión del
  linter; las tareas estaban `UP-TO-DATE`.
- La cobertura de errores de creación de usuario no comprueba detalles por
  campo ni ausencia de persistencia para DTO inválido; ver P-27.
- No se añadieron ni modificaron tests durante esta revisión.

## Validador: lint y formato

- `git diff --check`: sin errores de whitespace.
- `markdownlint` y `markdownlint-cli2` no están instalados; `npx --no-install`
  no produjo diagnóstico. No se pudo certificar el cero-warning de Markdown.
- La inspección estática de `1guides/11-pruebas.md` no encontró errores
  evidentes. `.markdownlint.json` desactiva MD041, MD013 y MD060.
- Build emite un warning: Kotlin no admite target JVM 24 y cae a JVM 22; hay
  incompatibilidad de target entre `compileJava` 24 y `compileKotlin` 22 en
  `buildSrc`. No impidió el build.

## Opcional

- El warning de finales de línea LF/CRLF corresponde al cambio local de
  `.agents/skills/qa-department/SKILL.md` y no afecta la ejecución.
- El MCP `postgres` no aparece entre los servidores, recursos o templates
  disponibles en esta sesión. La validación de filas, huérfanos, índices y
  constraints de la base queda sin ejecutar.

## Reconciliación propuesta (sin aplicar)

- Retirar P-29 de `pending.md`.
- Mantener P-15 como Alto y ampliar el contexto con las operaciones mutables
  anónimas de catálogo.
- Registrar los hallazgos P-23, P-25 y P-27 con ubicaciones y acciones.
- Registrar la semántica HTTP 204 y la exposición del driver PostgreSQL como
  deuda de menor prioridad, si el desarrollador confirma su inclusión.
- Revisar §4 de `MEMORY.md`: los commits completaron las invariantes de
  `UserService` y centralizaron P-03; P-26 quedó comentado. El punto sobre
  contrato HTTP sigue incompleto por `Location`. La nota que pide ignorar
  P-15 para el veredicto contradice el criterio BLOCK explícito de la skill
  `qa-department`.
