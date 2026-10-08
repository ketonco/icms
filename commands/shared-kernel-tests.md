# Tests de `shared-kernel`

## Ejecutar todos los tests

```powershell
.\gradlew.bat :shared-kernel:test
```

Ejecuta todos los tests del módulo `shared-kernel` (estructura, cobertura y
Locale de los bundles i18n; todos unitarios `*Test`).

## Pruebas puntuales (patrón `--tests`, acepta comodines `*`)

```powershell
.\gradlew.bat :shared-kernel:test --tests "com.icms.shared.i18n.MessageResolverLocaleTest"
.\gradlew.bat :shared-kernel:test --tests "com.icms.shared.i18n.MessageResolverLocaleTest.resolvesEnglishForUsLocale"
.\gradlew.bat :shared-kernel:test --tests "com.icms.shared.i18n.MessagesBundleStructureTest"
.\gradlew.bat :shared-kernel:test --tests "com.icms.shared.i18n.MessagesBundleCoverageTest"
.\gradlew.bat :shared-kernel:test --tests "com.icms.shared.i18n.*"
```

Para un método puntual usa `"Clase.metodo"`; para un paquete completo,
`"paquete.*"`. No agregues una línea por clase nueva: el patrón la cubre.
