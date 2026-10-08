# Tests del gateway con downstream controlado

Los tests del módulo `api` verifican el enrutado del gateway sin exigir que
`user-auth` esté levantado. Patrón real aplicado en
`api/src/test/java/com/icms/api/userauth/UserAuthGatewayRoutingIT.java`.

## 1. Por qué un stub y no el servicio real

La ruta `servicio-autenticacion-usuarios` apunta a
`http://${HOST_USER_AUTH:localhost}:${PORT_USER_AUTH:8081}` con predicado
`Path=/api/v1/auth/**` (`api/src/main/resources/application.yml`). Sin stub,
cada test exige `user-auth` vivo en `8081` más sus seeds (ej. `en-US` /
`English`), y la suite de `api` deja de ser autocontenida y determinista en
CI. Con stub se prueba lo propio del gateway (predicado, reenvío y
propagación de estado/cuerpo) y los controladores de `user-auth` los cubren
sus propios IT con RestAssured.

## 2. Dependencia de test

WireMock standalone (autocontenido, trae su Jetty; no interfiere con Netty):

```toml
# gradle/libs.versions.toml
[versions]
wiremock = "3.13.2"

[libraries]
wiremock-standalone = { module = "org.wiremock:wiremock-standalone", version.ref = "wiremock" }
```

```groovy
// api/build.gradle
dependencies {
    testImplementation libs.wiremock.standalone
}
```

Toda versión vive estrictamente en `gradle/libs.versions.toml` (§1 de
`.github/copilot-instructions.md`).

## 3. Paso a paso aplicado

1. Crear un `WireMockServer` con puerto dinámico (`dynamicPort()`), arrancado
   una vez por clase (`@BeforeAll`) y detenido en `@AfterAll`. Puerto fijo
   descartado: colisiona en CI paralela.
2. Registrar el stub como destino del gateway **antes** de que arranque el
   contexto Spring, con `@DynamicPropertySource` (`HOST_USER_AUTH=localhost`,
   `PORT_USER_AUTH=<puerto del stub>`). En `@BeforeEach` ya sería tarde: las
   rutas se resuelven al crear el contexto, y el puerto dinámico no se conoce
   en anotaciones fijas.
3. Programar un stub por endpoint con el shape `RestResponse` pactado
   (`ok(...)` / `okJson(...)`). Las rutas no usan `StripPrefix`, así que el
   gateway reenvía el path completo (`/api/v1/auth/test`).
4. Llamar `resetAll()` en `@BeforeEach` para aislar los tests entre sí.
5. Mantener `WebTestClient` contra el puerto random del gateway
   (`@LocalServerPort`) y las mismas aserciones de estado/cuerpo.
6. Ejecutar sin `user-auth` levantado:

```powershell
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIT"
```

## 4. Opciones no aplicadas y cuándo usarlas

- `MockWebServer` de OkHttp: más liviano, menos matchers de JSON/path.
  Preferible si el contrato es texto plano sin validación de cuerpo.
- Suite E2E con ambos servicios vivos: verificación ocasional de extremo a
  extremo, siempre aparte (perfil/sourceSet propio, excluida de
  `./gradlew test`), nunca como test del módulo.
- RestAssured en `:api`: descartado. Es la convención de módulos MVC
  bloqueantes; en WebFlux el cliente es `WebTestClient`.
- Riesgo de deriva stub-vs-real: el stub puede quedar viejo si cambia
  `user-auth`. Mitigación: contratos pactados mínimos y obvios junto al test,
  y un E2E manual para el día que se quiera el llamado real.

## 5. Checklist antes de darlo por listo

- [ ] Ningún test de `api` exige proceso externo ni seeds vivos.
- [ ] Stubs con puerto dinámico y `resetAll()` por test.
- [ ] Destino del gateway vía `@DynamicPropertySource`, no properties fijas.
- [ ] `RestResponse` pactado mínimo, sin copiar fixtures completas.
- [ ] Verde con `user-auth` apagado.
