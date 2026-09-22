plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

// 1. Obtenemos el catálogo de versiones 'libs' expuesto por Gradle
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

// 2. Leemos la variable 'java' definida en la sección [versions] del .toml
val javaVersion = libs.findVersion("java").get().requiredVersion.toInt()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

repositories {
    mavenCentral()
}