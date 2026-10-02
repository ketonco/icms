# Java y Gradle

## Java 24

La versión de Java se define en `gradle/libs.versions.toml`:

```toml
[versions]
java = "24"
```

El plugin de convención `buildSrc/src/main/kotlin/java-common-conventions.gradle.kts` lee esa versión y la aplica como toolchain a los módulos Java. Para cambiar la versión, actualiza el catálogo y comprueba la compatibilidad de las herramientas de compilación.

Comprueba el JDK disponible en PowerShell:

```powershell
java -version
Write-Output $env:JAVA_HOME
```

`JAVA_HOME` indica qué JDK usa Gradle para arrancar. El toolchain indica qué versión de Java usa para compilar. Asegúrate de que Java 24 esté instalado y disponible para Gradle.

## Gradle Wrapper

El proyecto fija la versión de Gradle en `gradle/wrapper/gradle-wrapper.properties`. Actualmente usa Gradle 8.14.3. Ejecuta las tareas con el wrapper para utilizar esa versión:

```powershell
.\gradlew.bat --version
.\gradlew.bat projects
.\gradlew.bat tasks
```

Comandos útiles para compilar módulos:

```powershell
.\gradlew.bat :user-auth:compileJava
.\gradlew.bat :api:bootJar
.\gradlew.bat build
```

Para ejecutar las pruebas:

```powershell
.\gradlew.bat test
```

## Ejecución con variables de usuario (`user-auth`)

Sin secretos en el repo (P-13). Crea 1 vez por usuario Windows y reinicia la terminal:

```powershell
[Environment]::SetEnvironmentVariable("DB_HOST","localhost","User")
[Environment]::SetEnvironmentVariable("DB_PORT","25432","User")
[Environment]::SetEnvironmentVariable("DB_USER","postgres","User")
[Environment]::SetEnvironmentVariable("DB_USER_AUTH_NAME","ICMS_UA","User")
[Environment]::SetEnvironmentVariable("DB_PASSWORD","xxx","User")
Get-ChildItem Env:DB_*
```

Ejecución local y normal usan las mismas variables; si falta `DB_PASSWORD` falla:

```powershell
.\gradlew.bat :user-auth:bootRun
$env:DB_PASSWORD="xxx"; .\gradlew.bat :user-auth:bootRun --args='--spring.profiles.active=prod'
```

En Docker no se usa `bootRun`; ver `05-docker-compose.md` (toma `api/.env` con `SPRING_PROFILES_ACTIVE=dev`).

## Diagnóstico básico

Si Gradle no detecta el JDK esperado, comprueba `java -version`, `JAVA_HOME` y la versión del toolchain en `gradle/libs.versions.toml`. Después de cambiar el JDK, puedes detener los daemons para que Gradle los inicie de nuevo:

```powershell
.\gradlew.bat --stop
```

No cambies el wrapper para resolver un problema de selección del JDK: el wrapper fija la versión de Gradle y el toolchain configura la versión de Java para compilar.
