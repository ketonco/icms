# Pendientes activos del proyecto ICMS

Revisión integral de `.github/copilot-instructions.md` frente al código real.
Fecha: 2026-10-05.

## P-03 — Versiones hardcodeadas fuera de `libs.versions.toml`

**Dónde:** `shared-kernel/build.gradle:12` y
`buildSrc/src/main/kotlin/migration-conventions.gradle.kts:16`

**Ubicacion del TODO: agregado** en `shared-kernel/build.gradle:12` y
`migration-conventions.gradle.kts:16`.

**Problema:** `shared-kernel/build.gradle:12` fija la versión del BOM con
`mavenBom '...:4.1.1'`, y
`migration-conventions.gradle.kts:16` declara directamente la coordenada y
versión de picocli en `liquibaseRuntime`, aunque ya existe el alias `libs.picocli`.
Esto contradice el uso estrictamente centralizado de versiones de §1.

**Contexto / Explicación:** versiones declaradas fuera del catálogo centralizado
se pueden desincronizar y hacen que el alias ya definido para picocli no cubra
la configuración de Liquibase.

**Opciones estándar:**

- A) Usar el catálogo para el BOM y el alias `libs.picocli` también en
  `liquibaseRuntime` (recomendado).
- B) Documentar la excepción en §1 si hay motivo técnico.

**Recomendación:** A, queda pendiente centralizar la versión del BOM y usar el
alias existente de picocli en todas sus configuraciones.

**Nivel de acción requerido:** Bajo — consistencia de build, sin impacto
funcional.

## P-15 — Rutas protegidas sin mecanismo de autenticación visible

**Dónde:**
`user-auth/src/main/java/com/icms/user_auth/config/SecurityConfig.java:49`

**Ubicacion del TODO: agregado** en `SecurityConfig.java:49`, antes de
deshabilitar login por formulario y HTTP Basic.

**Problema:** la configuración requiere autenticación para las rutas no
públicas, pero deshabilita form login y HTTP Basic. En `user-auth` no se
encontró un `AuthenticationProvider`, un `UserDetailsService` ni otro
mecanismo de autenticación configurado.

**Contexto / Explicación:** las solicitudes sin una identidad autenticada
reciben rechazo, y el módulo no presenta un flujo visible para establecer esa
identidad. La configuración de autorización por sí sola no permite
autenticar usuarios.

**Opciones estándar:**

- A) Implementar el mecanismo de autenticación previsto y conectarlo a esta
  cadena de filtros (recomendado).
- B) Habilitar temporalmente un mecanismo soportado por Spring Security
  mientras se implementa el flujo definitivo.

**Recomendación:** A, definiendo el mecanismo previsto antes de abrir las
rutas protegidas.

**Nivel de acción requerido:** Alto — impide el acceso autenticado a las rutas
protegidas y deja incompleto el flujo central del módulo.

## P-23 — Endpoint de creación responde HTTP 200 en vez de 201

**Donde y TODO:**
`user-auth/src/main/java/com/icms/user_auth/controller/UserController.java:28`,
`user-auth/src/test/java/com/icms/user_auth/controller/UserControllerTest.java:71`
y `shared-kernel/src/main/java/com/icms/shared/dto/RestResponse.java:26`;
TODO agregado en los tres archivos.

**Problema:** el método declara `@ResponseStatus(HttpStatus.CREATED)`, pero
devuelve `ResponseEntity.ok(...)`, cuyo estado explícito es 200. Además,
`RestResponse.ok` fija `status=200` en el cuerpo y la prueba espera 200.

**Contexto y explicación:** tanto el estado HTTP como el campo `status` del
cuerpo deben coincidir con el contrato de creación esperado. La prueba actual
no verifica el estado 201 ni el encabezado `Location`.

**Opciones estándar:**

- A) Devolver HTTP 201, propagarlo al campo `RestResponse.status`, incluir
  `Location` y ajustar la prueba del contrato (recomendado).
- B) Eliminar la anotación y documentar explícitamente 200 como contrato.

**Recomendación:** A, manteniendo alineados estado HTTP, cuerpo y encabezado de
la respuesta de creación.

**Nivel de acción requerido:** Medio — la respuesta no refleja en HTTP ni en el
cuerpo el estado de creación esperado.

## P-24 — Prueba de UserService no verifica invariantes de creación

**Donde y TODO:**
`user-auth/src/test/java/com/icms/user_auth/service/UserServiceTest.java:106`;
TODO agregado.

**Problema:** la prueba solo comprueba que el DTO devuelto no sea nulo y que su
nombre coincida; no verifica codificación de contraseña, asignación del rol y
estado predeterminados, ni exclusión de la contraseña de la respuesta.

**Contexto y explicación:** esos comportamientos son lógica propia de
`UserService` y afectan seguridad y consistencia de la cuenta creada. La prueba
actual puede pasar aunque las asignaciones o el tratamiento del secreto fallen.

**Opciones estándar:**

- A) Capturar/verificar la entidad guardada y las interacciones del mapper para
  comprobar esas invariantes con Mockito y AssertJ (recomendado).
- B) Añadir una prueba de integración de persistencia que valide el flujo
  completo de creación.

**Recomendación:** A para la lógica de servicio; mantener la prueba de
integración como validación del contrato HTTP.

**Nivel de acción requerido:** Alto — la prueba actual no protege invariantes de
seguridad ni de asignación de roles/estado.

## P-25 — Traducción ausente para el status DEL en el seed

**Donde y TODO:**
`user-auth/src/main/java/com/icms/user_auth/cli/level2/UserStatusTranslationDataSeed.java:61`;
TODO agregado.

**Problema:** el seed `UserStatusDataSeed` declara el status `DEL`, pero
`UserStatusTranslationDataSeed` solo carga traducciones para `ACT`, `INA` y
`SUS`. El código actual no declara `PENDING`; al crear usuarios se asigna
`INA`, que sí cuenta con traducciones en el seed.

**Contexto y explicación:** el endpoint público de estados podría mostrar el
status `DEL` sin nombre traducido después de ejecutar los seeds. No se pudo
confirmar el contenido actual de la base porque el MCP de PostgreSQL no está
disponible en esta sesión.

**Opciones estándar:**

- A) Agregar traducciones para `DEL` en el seed, en los idiomas activos
  pertinentes (recomendado para el flujo de desarrollo local).
- B) Si una base compartida o productiva ya fue inicializada, agregar también
  las filas faltantes mediante un changeset Liquibase.

**Recomendación:** A para que el seed mantenga completos los datos desde el
reinicio local; verificar la BD con PostgreSQL MCP cuando esté disponible.

**Nivel de acción requerido:** Medio — el catálogo y su seed de traducciones
no cubren el mismo conjunto de estados.

## P-26 — Dependencia WebFlux de pruebas sin uso en user-auth

**Donde y TODO:** `user-auth/build.gradle:24`; TODO agregado.

**Problema:** `user-auth` declara `spring-boot-starter-webflux` en
`testImplementation`, pero no se encontraron usos de `WebTestClient` en las
pruebas de ese módulo.

**Contexto y explicación:** Copilot y AGENTS solo permiten WebFlux en las
pruebas MVC cuando se usa como cliente `WebTestClient`. La dependencia actual
añade una pila reactiva de pruebas que el módulo no usa.

**Opciones estándar:**

- A) Eliminar la dependencia si no existe una prueba que requiera
  `WebTestClient` (recomendado).
- B) Mantenerla únicamente si se incorpora una prueba de cliente WebTestClient
  justificada en `user-auth`.

**Recomendación:** A, al no haber usos de `WebTestClient` en las pruebas
actuales del módulo.

**Nivel de acción requerido:** Bajo — dependencia de prueba innecesaria que
debilita el aislamiento de pilas indicado para el módulo.

## P-27 — Prueba de controlador no cubre el rechazo de DTO inválidos

**Donde y TODO:**
`user-auth/src/test/java/com/icms/user_auth/controller/UserControllerTest.java:76`;
TODO agregado.

**Problema:** `UserControllerTest` solo envía un DTO válido y no comprueba que
las restricciones declaradas en `CreateUserDto` y su perfil anidado produzcan
un rechazo.

**Contexto y explicación:** el endpoint ahora activa `@Valid`, pero la prueba
actual no detectaría si se elimina esa validación o si deja de ejecutarse para
campos anidados.

**Opciones estándar:**

- A) Añadir casos RestAssured con campos requeridos vacíos, formato de email o
  contraseña inválidos y perfil anidado inválido; comprobar HTTP 400 y que no se
  persista el usuario (recomendado).
- B) Cubrir las restricciones solo con pruebas unitarias del validador.

**Recomendación:** A, comprobando el contrato HTTP y la ausencia de persistencia
para solicitudes inválidas.

**Nivel de acción requerido:** Medio — la validación es lógica nueva del
controlador sin una prueba negativa que la proteja.

## P-28 — Rutas de prueba expuestas en la configuración de producción

**Donde y TODO:**
`api/src/main/java/com/icms/api/config/TestRouterConfig.java:15`,
`user-auth/src/main/java/com/icms/user_auth/controller/TestController.java:11`
y `user-auth/src/main/java/com/icms/user_auth/config/SecurityConfig.java:44`;
TODO agregado en las tres ubicaciones.

**Problema:** el Gateway publica una ruta local `/test` y `user-auth` publica
`/api/v1/auth/test`; ambas configuraciones están activas fuera del perfil de
pruebas y el endpoint de `user-auth` además está permitido explícitamente.

**Contexto y explicación:** son endpoints diagnósticos con respuestas fijas,
incluidos en la aplicación desplegable. El test actual del Gateway usa un
downstream WireMock y ya no necesita ejecutar el controlador real.

**Opciones estándar:**

- A) Restringir las rutas al perfil `test` o quitarlas de la configuración de
  producción (recomendado).
- B) Sustituirlas por un mecanismo de salud dedicado y documentado.

**Recomendación:** A para las rutas de fixture; usar un endpoint de salud
dedicado si se necesita monitorización en ejecución.

**Nivel de acción requerido:** Bajo — publica endpoints de prueba en el
despliegue normal y mantiene innecesariamente un permiso anónimo.
