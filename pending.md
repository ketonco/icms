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
