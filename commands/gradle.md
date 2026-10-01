# Gradle — proyecto principal

## Detener procesos de Gradle

```powershell
.\gradlew.bat --stop
```

Detiene los Gradle Daemons en ejecución. Útil después de cambios en la
configuración de Gradle o para liberar recursos.

## Limpiar artefactos generados

```powershell
.\gradlew.bat clean
```

Elimina los directorios `build` generados por todos los módulos del monorepo.

## Listar módulos del monorepo

```powershell
./gradlew projects
```

Muestra la lista de proyectos o módulos anidados configurados en el proyecto
principal de Gradle.

## Refrescar Dependencias

```powershell
./gradlew --refresh-dependencies build -x test
```

Refresca las dependencias de cada modulo en un solo comando, bastante util al agregar una dependencia nueva ejecutarlo

## Reporte de dependencias

```powershell
./gradlew build --scan
```

Gradle genera un reporte web detallado e interactivo de todo el árbol de dependencias de tus microservicios. Nos permitirá auditar visualmente que ningún módulo esté arrastrando dependencias duplicadas o versiones no deseadas sin adivinar nada.

## Generacion de archivo jar

```powershell
./gradlew :api:bootJar --no-daemon
```

Compila el módulo `api` y genera su archivo JAR ejecutable de Spring Boot. La
opción `--no-daemon` ejecuta Gradle sin utilizar un proceso daemon persistente,
por lo que resulta útil en compilaciones puntuales o entornos de CI.
