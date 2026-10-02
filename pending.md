# Pendientes activos del proyecto ICMS

Revisión integral de `.github/copilot-instructions.md` frente al código real.
Fecha: 2026-10-02.

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

## P-19 — UserProfileService no tiene pruebas para su lógica propia

**Dónde:**
`user-auth/src/test/java/com/icms/user_auth/service/UserProfileServiceTest.java:6-8`

**Ubicación del TODO: no agregado.**

**Problema:** la clase de prueba está vacía, aunque `UserProfileService` tiene
lógica propia para guardar, actualizar y resolver usuario y perfil.

**Contexto y explicación:** los casos de ausencia de usuario o perfil y la
conversión del DTO no tienen cobertura específica conforme al principio
selectivo de pruebas de §I.

**Opciones estándar:**

- A) Añadir pruebas unitarias con Mockito y AssertJ para las rutas propias
  (recomendado).
- B) Mantener la clase vacía y confiar únicamente en pruebas de integración.

**Recomendación:** A, verificando guardado/actualización y excepciones cuando
falte el usuario o perfil.

**Nivel de acción requerido:** Medio — lógica propia de servicio sin pruebas
específicas.

## P-20 — Prueba de gateway depende de un servicio externo iniciado aparte

**Dónde:**
`api/src/test/java/com/icms/api/userauth/UserAuthGatewayRoutingIntegrationTest.java:36,55,82`

**Ubicación del TODO: no agregado.**

**Problema:** las pruebas de integración requieren que `user-auth` esté
ejecutándose previamente en `localhost:8081`.

**Contexto y explicación:** la prueba combina el gateway con un proceso externo
y datos de ese servicio, por lo que no es aislada ni determinista al ejecutar
la suite del módulo API.

**Opciones estándar:**

- A) Proveer un downstream controlado durante la prueba (recomendado).
- B) Mantener una suite explícita de sistema que arranque ambos servicios como
  parte de su configuración.

**Recomendación:** A para la prueba del módulo; reservar B para una suite de
extremo a extremo independiente.

**Nivel de acción requerido:** Medio — la prueba falla si no se prepara un
servicio externo y su estado esperado.
