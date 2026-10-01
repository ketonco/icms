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

## P-12 — `UserMapperTest` inicializa Mockito manualmente

**Dónde:** `user-auth/src/test/java/com/icms/user_auth/mappers/UserMapperTest.java:34-44`

**Ubicacion del TODO: agregado** en `UserMapperTest.java:43`.

**Problema:** el test inicializa Mockito mediante `MockitoAnnotations.openMocks(this)` en `@BeforeEach`, mientras que `.github/copilot-instructions.md` establece `@ExtendWith(MockitoExtension.class)` para pruebas unitarias con Mockito.

**Contexto / Explicación:** el TODO anterior era genérico y no definía qué debía validarse; el patrón indicado evita la inicialización manual de los mocks.

**Opciones estándar:**

- A) Usar `@ExtendWith(MockitoExtension.class)` y retirar `@BeforeEach` con `openMocks` (recomendado).
- B) Mantener la inicialización manual y documentar una excepción para esta prueba.

**Recomendación:** A, para alinear el test con la convención del proyecto.

**Nivel de acción requerido:** Bajo — consistencia de pruebas, sin impacto en producción.

## P-13 — Contraseña de base de datos predeterminada en configuración

**Dónde:** `user-auth/src/main/resources/application.yml:8` y
`user-auth/build.gradle:46`

**Ubicacion del TODO: agregado** en ambos valores de contraseña
predeterminados.

**Problema:** la configuración de ejecución y la actividad Liquibase contienen
un valor de contraseña fijo como alternativa cuando no se proporciona
`DB_PASSWORD` o `dbPassword`. `AGENTS.md` prohíbe incluir contraseñas
hardcodeadas en archivos de configuración que no sean de ejemplo.

**Contexto / Explicación:** si una ejecución no establece explícitamente la
variable o propiedad, la aplicación o Liquibase intentarán autenticarse con la
contraseña predeterminada. El valor versionado no debe servir como credencial
de entornos compartidos.

**Opciones estándar:**

- A) Exigir la variable o propiedad fuera de los entornos locales y mantener
  los valores locales fuera de la configuración versionada (recomendado).
- B) Mover los valores de desarrollo a un archivo de ejemplo no usado
  directamente por la aplicación.

**Recomendación:** A, para que las ejecuciones no locales fallen si falta una
credencial externa.

**Nivel de acción requerido:** Alto — evita el uso accidental de una
contraseña fija en entornos no locales.

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
