---
applyTo: "gradle/**, buildSrc/**, **/build.gradle, **/build.gradle.kts, settings.gradle, settings.gradle.kts"
---

# Gradle y dependencias

- Usa el Gradle Wrapper del repositorio. Conserva el monorepo multi-project, el catálogo gradle/libs.versions.toml y los plugins de convención Kotlin DSL en buildSrc.
- No declares versiones sueltas en los build.gradle de módulos cuando la dependencia corresponde al catálogo central. Comprueba primero si ya existe alias o plugin de convención aplicable.
- Antes de proponer una librería nueva o cambiar el catálogo, explica la necesidad técnica, presenta al menos dos opciones viables y espera aprobación explícita antes de editar gradle/libs.versions.toml.
- No cambies toolchain ni versión oficial de Java por diferencias locales de entorno. El estándar del proyecto es Java 24.
- Consulta 1guides/02-java-y-gradle.md y 1guides/08-dependencias.md para comandos, ámbitos y criterios de ubicación.
