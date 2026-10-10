plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
}

dependencies {
    implementation(libs.boot.starter.data.jdbc)
    implementation(libs.boot.starter.webmvc)

    runtimeOnly(libs.boot.starter.flyway)
    runtimeOnly(libs.boot.starter.thymeleaf)
    runtimeOnly(libs.boot.postgresql)
    runtimeOnly(libs.boot.flyway.postgresql)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(libs.boot.starter.webmvc.test)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)
}

tasks.test {
    useJUnitPlatform()
}
