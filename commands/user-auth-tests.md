# Tests de `user-auth`

## Ejecutar todos los tests

```powershell
.\gradlew.bat :user-auth:test
```

Ejecuta todos los `Test` del modulo de `user-auth`.

## Pruebas puntuales (patrón `--tests`, acepta comodines `*`)

```powershell
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.controller.TestControllerIT"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.mappers.LanguageMapperTest.createEntityFromDto"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.service.*"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.repository.LanguageRepositoryIT"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.rules.UserStatusTranslationRulesTest"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.controller.UserAuthGlobalExceptionsIT"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.dto.*"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.i18n.MessagesI18nIT"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.UserAuthApplicationIT"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.controller.*"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.rules.*"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.mappers.*"
.\gradlew.bat :user-auth:test --tests "com.icms.user_auth.repository.*"
```

Para un método puntual usa `"Clase.metodo"`; para un paquete completo, `"paquete.*"`.
No agregues una línea por clase nueva: el patrón la cubre.
