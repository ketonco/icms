# Pendientes activos del proyecto ICMS

Revisión integral de `.github/copilot-instructions.md` frente al código real.
Fecha: 2026-10-09.

## P-15 — Autenticación pendiente y escrituras de catálogo anónimas

**Donde y TODO:**
`user-auth/src/main/java/com/icms/user_auth/config/SecurityConfig.java:39-50`;
TODO agregado en la línea 48.

**Problema:** las rutas no públicas requieren autenticación, pero no existe un
`AuthenticationProvider`, `UserDetailsService` ni otro mecanismo visible en
`user-auth`. Además, las rutas `/api/v1/auth/languages/**` y
`/api/v1/auth/user-status-translations/**` permiten acceso anónimo e incluyen
operaciones de escritura, actualización y eliminación.

**Contexto y explicación:** deshabilitar login por formulario y HTTP Basic sin
configurar el mecanismo previsto deja las rutas protegidas inaccesibles para
usuarios legítimos. A la vez, `permitAll` en los catálogos expone operaciones
mutables que pueden alterar datos de idioma y traducciones sin identidad.

**Opciones estándar:**

- A) Implementar el mecanismo de autenticación/autoridad previsto y permitir
  anónimamente solo las lecturas públicas requeridas (recomendado).
- B) Restringir temporalmente las operaciones mutables mientras se completa el
  flujo de autenticación.

**Recomendación:** A, con login/token, autorización por operación y pruebas que
confirmen 401/403 para escrituras anónimas y acceso público solo a las lecturas
definidas.

**Nivel de acción requerido:** Alto — bloquea la QA preproducción y expone
operaciones mutables de catálogos sin autenticación.

## P-23 — Contrato HTTP de creación sin cabecera Location

**Donde y TODO:**
`user-auth/src/main/java/com/icms/user_auth/controller/UserController.java:28-31`,
`user-auth/src/test/java/com/icms/user_auth/controller/UserControllerIT.java:56-60`
y `shared-kernel/src/main/java/com/icms/shared/dto/RestResponse.java:37`;
TODO agregado en `RestResponse.java:37`.

**Problema:** el endpoint devuelve HTTP 201 y el cuerpo fija `status=201`, pero
no incluye `Location`. La prueba comprueba el estado HTTP y algunos campos de
`data`, pero omite el estado del cuerpo y la cabecera `Location`.

**Contexto y explicación:** clientes que siguen el contrato REST de creación no
pueden descubrir desde la respuesta la URI del recurso recién creado. La
prueba actual tampoco protege esos elementos del contrato.

**Opciones estándar:**

- A) Construir `Location` desde la URI del recurso y verificar HTTP 201,
  `body.status=201`, datos y cabecera en `UserControllerIT` (recomendado).
- B) Documentar explícitamente un contrato de creación sin `Location` y
  eliminar ese requisito de `MEMORY.md`.

**Recomendación:** A, manteniendo alineados implementación, documentación y
prueba HTTP.

**Nivel de acción requerido:** Medio — la creación funciona, pero la respuesta
no completa el contrato documentado.

## P-27 — Tests de DTO inválido no verifican errores por campo ni ausencia de persistencia

**Donde y TODO:**
`user-auth/src/test/java/com/icms/user_auth/controller/UserControllerIT.java:76-132`;
TODO no agregado.

**Problema:** los casos con username/email inválidos verifican HTTP 400 y un
mensaje genérico, pero no comprueban el mapa `errors` con el campo que falló ni
que el usuario no haya sido persistido.

**Contexto y explicación:** los tests pueden pasar aunque se pierda el detalle
de validación por propiedad o se ejecute parcialmente el flujo de creación
antes del rechazo.

**Opciones estándar:**

- A) Afirmar los errores por campo y consultar `UserRepository` para comprobar
  que no existe el usuario tras cada petición inválida (recomendado).
- B) Probar por separado la estructura del `RestResponse` y delegar la
  persistencia a tests de servicio.

**Recomendación:** A para cubrir el contrato HTTP y el efecto observable de la
solicitud inválida.

**Nivel de acción requerido:** Medio — la prueba negativa no protege el detalle
de validación ni que no se persista el usuario.

## P-31 — Respuestas HTTP 204 construidas con cuerpo

**Donde y TODO:**
`shared-kernel/src/main/java/com/icms/shared/controller/ReadController.java:20-24,30-34`;
TODO no agregado.

**Problema:** los casos sin resultados devuelven estado HTTP 204 junto con
`RestResponse.noContent()` como cuerpo. HTTP 204 no transporta cuerpo, de modo
que el wrapper y el mensaje G-002 no son observables para el cliente.

**Contexto y explicación:** el estado HTTP y el cuerpo prometen dos contratos
distintos; la respuesta efectiva puede descartar el cuerpo. No hay una prueba
que fije cuál comportamiento deben consumir los clientes.

**Opciones estándar:**

- A) Responder 204 sin cuerpo y ajustar el contrato/documentación
  (recomendado si se conserva ese status).
- B) Usar un status que admita cuerpo, como 200, si se necesita devolver
  `RestResponse.noContent()`.

**Recomendación:** elegir y probar un único contrato para listas y recursos
individuales sin resultado.

**Nivel de acción requerido:** Bajo — discrepancia de contrato en respuestas
vacías; no afecta los casos con datos.

## P-32 — PostgreSQL JDBC 42.7.4 afectado por CVE-2026-54291

**Donde y TODO:** `gradle/libs.versions.toml:9` y
`buildSrc/src/main/kotlin/spring-jpa-conventions.gradle.kts:10`;
TODO no agregado.

**Problema:** el driver `org.postgresql:postgresql` está fijado en `42.7.4`,
versión afectada hasta `42.7.11` por una degradación de channel binding bajo
`channelBinding=require` y condiciones específicas de intermediario TLS.

**Contexto y explicación:** el driver se incluye en runtime de `user-auth`. No
se confirmó que el proyecto configure `channelBinding=require`; el advisory
indica que la versión corregida es `42.7.12`.

**Opciones estándar:**

- A) Actualizar la dependencia centralizada a `42.7.12` o una versión posterior
  compatible (recomendado).
- B) Mantener temporalmente la versión solo tras verificar TLS con
  `sslmode=verify-full` y una CA de confianza, y confirmar que no se depende de
  channel binding como única protección.

**Recomendación:** A y validar la negociación SCRAM/TLS en el entorno de
preproducción.

**Nivel de acción requerido:** Medio — vulnerabilidad de severidad alta pero
condicional al modo de channel binding y a un escenario de intermediario.
