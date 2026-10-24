plugins {
    application
}

dependencies {
    implementation(libs.slf4j)
    runtimeOnly(libs.logback)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito)
    testRuntimeOnly(libs.junit.launcher)
}

application {
    mainClass = "ru.otus.Main"
}

tasks.test {
    useJUnitPlatform()
}
