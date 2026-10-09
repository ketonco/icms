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
