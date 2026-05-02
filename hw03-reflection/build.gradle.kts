plugins {
    application
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

application {
    mainClass = "ru.otus.Main"
}

tasks.test {
    useJUnitPlatform()
}