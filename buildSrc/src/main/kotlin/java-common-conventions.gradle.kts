plugins {
    java
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val javaVersion = libs.findVersion("java").get().requiredVersion.toInt()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

repositories {
    mavenCentral()
}// Banderas necesarias para procesadores de anotaciones en JDKs recientes (Java 24)

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(
        listOf(
            "--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED"
        )
    )
}

dependencies {
    compileOnly(libs.findLibrary("lombok").get())

    implementation(libs.findLibrary("mapstruct").get())
    implementation(libs.findLibrary("spring-boot-starter-validation").get())
    implementation(libs.findLibrary("jackson-databind").get())
    implementation(libs.findLibrary("jackson-datatype-jsr310").get())

    annotationProcessor(libs.findLibrary("lombok").get())
    annotationProcessor(libs.findLibrary("lombok-mapstruct-binding").get())
    annotationProcessor(libs.findLibrary("mapstruct-processor").get())

    testCompileOnly(libs.findLibrary("lombok").get())
    testAnnotationProcessor(libs.findLibrary("lombok").get())

}

tasks.named<Test>("test") {
    useJUnitPlatform()
}