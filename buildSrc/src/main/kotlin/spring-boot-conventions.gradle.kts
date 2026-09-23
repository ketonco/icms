plugins {
    id("java-common-conventions")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
dependencies {
    testImplementation(libs.findLibrary("spring-boot-starter-test").get())
}