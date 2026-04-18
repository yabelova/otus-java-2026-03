plugins {
    application
}

dependencies {
    implementation(project(":l02-lib"))
    implementation(libs.slf4j)

    runtimeOnly(libs.logback)
    runtimeOnly(libs.logstash)

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