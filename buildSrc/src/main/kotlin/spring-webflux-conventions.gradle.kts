plugins {
    id("spring-boot-conventions")
}
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val springCloudVersion = libs.findVersion("springCloud").get().requiredVersion

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}
dependencies {
    implementation(libs.findLibrary("spring-boot-starter-webflux").get())

    testImplementation(libs.findLibrary("reactor-test").get())
    testImplementation(libs.findLibrary("spring-boot-starter-webflux").get())
}