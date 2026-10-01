# Tests de `user-auth`

## Ejecutar todos los tests

```powershell
.\gradlew.bat :user-auth:test
```

Ejecuta todos los `Test` del modulo de `user-auth`.

## Pruebas puntuales (patrón `--tests`, acepta comodines `*`)

```powershell
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.controller.TestControllerIntegrationTest"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.mappers.LanguageMapperTest.createEntityFromDto"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.service.*"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.repository.LanguageRepositoryTest"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.rules.UserStatusTranslationRulesTest"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.exceptions.UserAuthGlobalExceptionsIT"
```

Para un método puntual usa `"Clase.metodo"`; para un paquete completo, `"paquete.*"`.
No agregues una línea por clase nueva: el patrón la cubre.
