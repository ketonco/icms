//indicamos que este proyecto utiliza el DSL de Kotlin, es decir que el archivo de construcción está escrito en Kotlin (.gradle.kts)
plugins { 
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

kotlin {
    jvmToolchain(24)
}

dependencies {
    implementation(libs.spring.boot.gradle.plugin)
    implementation(libs.dependency.management.gradle.plugin)
    implementation(libs.liquibase.gradle.plugin)
}