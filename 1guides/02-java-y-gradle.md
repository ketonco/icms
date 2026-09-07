# Java y Gradle

## Java 24

Los modulos usan Java 24 mediante Gradle toolchains:

```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}
```

Comprobar el JDK local:

```powershell
java -version
Write-Output $env:JAVA_HOME
```

## Wrapper

```powershell
.\gradlew.bat tasks
.\gradlew.bat :user-auth:compileJava
.\gradlew.bat :api:bootJar
```

`JAVA_HOME` indica con que JDK arranca Gradle; `JavaLanguageVersion.of(24)` indica con que version se compilan las tareas Java. Si un daemon viejo causa confusion, ejecutar `.\gradlew.bat --stop`.

Si Gradle pide Java 21, revisar los `build.gradle` y corregir el toolchain del modulo a `JavaLanguageVersion.of(24)`. No cambiar el wrapper para resolverlo.
