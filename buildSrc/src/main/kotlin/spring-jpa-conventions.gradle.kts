plugins {
    id("spring-boot-conventions")
}
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
dependencies {

    implementation(libs.findLibrary("spring-boot-starter-data-jpa").get())
    implementation(libs.findLibrary("spring-data-envers").get())

    runtimeOnly(libs.findLibrary("postgresql").get())

    testImplementation(libs.findLibrary("spring-boot-starter-data-jpa-test").get())
    testImplementation(libs.findLibrary("spring-boot-starter-data-jpa").get())
}