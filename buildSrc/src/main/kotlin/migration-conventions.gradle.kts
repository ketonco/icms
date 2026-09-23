plugins {
    id("java-common-conventions")
    id("org.liquibase.gradle")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("spring-boot-starter-liquibase").get())
    implementation(libs.findLibrary("liquibase-core").get())

    runtimeOnly(libs.findLibrary("picocli").get())

	// Requerido por la tarea de Liquibase para ejecutar desde CLI
    liquibaseRuntime(libs.findLibrary("liquibase-core").get())
    liquibaseRuntime("info.picocli:picocli:4.7.6")
    liquibaseRuntime(sourceSets.main.get().output)

    testImplementation(libs.findLibrary("spring-boot-starter-liquibase-test").get())
}
