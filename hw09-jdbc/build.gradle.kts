plugins {
    application
}

dependencies {
    implementation(libs.slf4j)
    runtimeOnly(libs.logback)

    implementation(libs.flyway.core)
    implementation(libs.hikari)
    runtimeOnly(libs.flyway.postgresql)
    runtimeOnly(libs.postgresql)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
    testImplementation(libs.assertj)
    testImplementation(libs.mockito)
}

application {
    mainClass = "ru.otus.HomeWork"
}

tasks.test {
    useJUnitPlatform()
}
