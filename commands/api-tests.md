# Tests de `api`

## Ejecutar todos los tests

```powershell
.\gradlew.bat :api:test
```

Ejecuta todos los tests del módulo `api` (unitarios `*Test` e integración
`*IT`). Los tests del gateway usan downstream controlado (WireMock) y no
requieren `user-auth` vivo; detalle en `1guides/10-gateway-tests.md`.

## Pruebas puntuales (patrón `--tests`, acepta comodines `*`)

```powershell
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIT"
.\gradlew.bat :api:test --tests "com.icms.api.userauth.UserAuthGatewayRoutingIT.testGatewayRoutingToUserAuth"
.\gradlew.bat :api:test --tests "com.icms.api.userauth.*"
.\gradlew.bat :api:test --tests "com.icms.api.ApiApplicationIT"
```

Para un método puntual usa `"Clase.metodo"`; para un paquete completo,
`"paquete.*"`. No agregues una línea por clase nueva: el patrón la cubre.
