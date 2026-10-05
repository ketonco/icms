# Pendientes activos del proyecto ICMS

Revisión integral de `.github/copilot-instructions.md` frente al código real.
Fecha: 2026-10-05.

## P-03 — Versiones hardcodeadas fuera de `libs.versions.toml`

**Dónde:** `shared-kernel/build.gradle:12` y
`buildSrc/src/main/kotlin/migration-conventions.gradle.kts:16`

**Ubicacion del TODO: no agregado** (aparcado, lo revisa el desarrollador)

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
`user-auth/src/main/java/com/icms/user_auth/config/SecurityConfig.java:51`

**Ubicacion del TODO: agregado** antes de deshabilitar login por formulario y
HTTP Basic.

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
`user-auth/src/main/java/com/icms/user_auth/controller/UserController.java:23-26`
y `user-auth/src/test/java/com/icms/user_auth/controller/UserControllerTest.java:71-73`;
TODO agregado en la prueba, línea 71.

**Problema:** el método declara `@ResponseStatus(HttpStatus.CREATED)`, pero
devuelve `ResponseEntity.ok(...)`, cuyo estado explícito es 200; la prueba espera
200 y deja pendiente cambiarlo a 201.

**Contexto y explicación:** el estado HTTP observado contradice la intención
expresada por la anotación y el contrato esperado para crear un recurso.

**Opciones estándar:**

- A) Devolver `ResponseEntity.status(HttpStatus.CREATED)` y ajustar la prueba a
  201 (recomendado).
- B) Eliminar la anotación y documentar explícitamente 200 como contrato.

**Recomendación:** A, manteniendo el estado estándar de creación.

**Nivel de acción requerido:** Medio — el recurso se crea, pero la respuesta
HTTP no refleja el resultado de creación.

## P-24 — Prueba de UserService no verifica invariantes de creación

**Donde y TODO:**
`user-auth/src/test/java/com/icms/user_auth/service/UserServiceTest.java:103-108`;
TODO no agregado.

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
