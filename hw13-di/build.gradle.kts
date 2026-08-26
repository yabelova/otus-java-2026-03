plugins {
    application
}

dependencies {
    implementation(libs.reflections)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
    testImplementation(libs.assertj)
    testImplementation(libs.mockito)
}

application {
    mainClass = "ru.otus.App"
}

tasks.test {
    useJUnitPlatform()
}
