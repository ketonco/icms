plugins {
    id("spring-boot-conventions")
}
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
dependencies {
    implementation(libs.findLibrary("spring-boot-starter-web").get())
}