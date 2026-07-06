plugins {
    application
    alias(libs.plugins.jmh)
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

    "jmhAnnotationProcessor"(libs.jmh.annprocess)
    "jmhImplementation"(libs.jmh.core)
    "jmhImplementation"(libs.testcontainers.postgresql)
}

application {
    mainClass = "ru.otus.DbServiceCacheDemo"
}

tasks.test {
    useJUnitPlatform()
}

jmh {
    warmupIterations = 1
    iterations = 3
    fork = 1
    resultFormat.set("text")
}