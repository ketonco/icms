# Pendientes activos del proyecto ICMS

Revisión integral de `.github/copilot-instructions.md` frente al código real.
Fecha: 2026-10-05.

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
`user-auth/src/test/java/com/icms/user_auth/controller/UserControllerIT.java:71`
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
`user-auth/src/test/java/com/icms/user_auth/controller/UserControllerIT.java:76`;
TODO agregado.

**Problema:** `UserControllerIT` solo envía un DTO válido y no comprueba que
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

## P-29 — Quince claves huérfanas en los bundle i18n

**Donde y TODO:**
`shared-kernel/src/main/resources/i18n/messages.properties:14,19,26-30,36-43`
(líneas anteriores a la estandarización) y su espejo `messages_es.properties`;
sin TODO en código, la corrección es eliminar las claves.

**Problema:** quince claves definidas en los bundle sin ninguna referencia en
`src/main/java` ni en los tests: `Ent-002`, `Ent-007`, `Lan-001` a `Lan-005` y
`Usr-001` a `Usr-008`.

**Contexto y explicación:** son texto muerto que oculta qué códigos están real
mente en uso y dejaba huecos en la numeración de los prefijos, impidiendo la
numeración continua `001..NNN` que exige la convención de códigos.

**Opciones estándar:**

- A) Eliminarlas en la estandarización y declarar cualquier clave reservada en
  `RESERVED_MESSAGE_KEYS` del test de cobertura (recomendado).
- B) Conservarlas como reservadas con una lista de excepciones propia.

**Recomendación:** A, aplicada en el refactor `messages-properties-usecases`;
las quince claves dejan de existir en ambos bundle. Si más adelante se necesita
una clave nueva sin usar todavía, debe declararse en `RESERVED_MESSAGE_KEYS`
o fallará `MessagesBundleCoverageTest`.

**Nivel de acción requerido:** Bajo — claves muertas, sin impacto en
ejecución; solo ruido y numeración inconsistente.

## P-30 — Texto EN/ES incoherente en cuatro claves de validación `.null`

**Donde y TODO:**
`shared-kernel/src/main/resources/i18n/messages.properties` en las claves
`usrstatustrans.catalogid.null`, `usrstatustrans.languageid.null`,
`usrtypetrans.catalogid.null` y `usrtypetrans.languageid.null`; sin TODO en
código.

**Problema:** en inglés los cuatro valores dicen `cannot be blank` y en español
dicen `no puede ser nulo`, mientras que la anotación asociada es `@NotNull` y
la regla de la propia clave es `.null`.

**Contexto y explicación:** quien recibe la respuesta en inglés ve "no debe
estar vacío" para un error de nulidad, y la regla de naming
`<dominio>.<campo>.<regla>` queda contradicha por el texto del mensaje.

**Opciones estándar:**

- A) Unificar los textos en inglés a `... cannot be null` y actualizar las
  cuatro aserciones afectadas en `UserTypeTranslationDtoValidationIT:78,110` y
  `UserStatusTranslationDtoValidationIT:78,110` (recomendado).
- B) Cambiar la regla de la clave a `.blank` y la anotación a `@NotBlank`, lo
  que altera el comportamiento de validación.

**Recomendación:** A, en una tarea propia para no mezclar cambios de texto con
la renumeración de códigos del ciclo `messages-properties-usecases`.

**Nivel de acción requerido:** Bajo — traducción confusa, sin impacto
funcional.
