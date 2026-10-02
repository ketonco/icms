# PENDING.md — Pendientes activos del proyecto ICMS

Validación de `.github/copilot-instructions.md` vs código real. Fecha: 2026-09-30.
Solo lectura y marcadores `TODO`; nada de lo listado está corregido todavía.

## P-03 — Versiones hardcodeadas fuera de `libs.versions.toml`

**Dónde:** `shared-kernel/build.gradle:12` y
`buildSrc/src/main/kotlin/migration-conventions.gradle.kts:16`

**Ubicacion del TODO: no agregado** (aparcado, lo revisa el desarrollador)

**Problema:** `shared-kernel/build.gradle:12` fija `mavenBom '...:4.1.1'` y
`migration-conventions.gradle.kts:16` fija
`liquibaseRuntime("info.picocli:picocli:4.7.6")` en vez de usar el catálogo
`libs`, lo que contradice el “estrictamente `libs.versions.toml`” de §1.

**Contexto / Explicación:** las versiones dispersas fuera del catálogo
centralizado se desincronizan con el tiempo y rompen la regla de §1.

**Opciones estándar:**

- A) Migrar ambas a `libs` (recomendado).
- B) Documentar la excepción en §1 si hay motivo técnico.

**Recomendación:** A, pendiente de revisión del desarrollador.

**Nivel de acción requerido:** Bajo — consistencia de build, sin impacto
funcional.

## P-14 — Stack traces habilitados en la configuración base

**Dónde:** `user-auth/src/main/resources/application.yml:26`

**Ubicacion del TODO: agregado** en la propiedad
`spring.mvc.problem.include-stacktrace`.

**Problema:** `include-stacktrace: always` está declarado en la configuración
base y no limitado a perfiles de desarrollo o diagnóstico, como requiere la
sección K de `.github/copilot-instructions.md`.

**Contexto / Explicación:** los perfiles activos heredan la configuración
base; las respuestas de error podrían revelar rutas internas, nombres de
clases y detalles de implementación.

**Opciones estándar:**

- A) Activar los stack traces solo mediante una configuración de perfil de
  desarrollo o diagnóstico (recomendado).
- B) Deshabilitarlos en la configuración base y habilitarlos explícitamente al depurar.

**Recomendación:** A, manteniendo el diagnóstico detallado fuera de perfiles
compartidos o productivos.

**Nivel de acción requerido:** Alto — puede exponer información interna en
respuestas de error.

## P-15 — Rutas protegidas sin mecanismo de autenticación visible

**Dónde:**
`user-auth/src/main/java/com/icms/user_auth/config/SecurityConfig.java:46-50`

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
