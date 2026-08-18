plugins {
    application
}

dependencies {
    implementation(libs.slf4j)
    runtimeOnly(libs.logback)

    implementation(libs.hibernate.core)
    runtimeOnly(libs.postgresql)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(libs.jetty.ee10.servlet)
    implementation(libs.jetty.server)
    implementation(libs.freemarker)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
    testImplementation(libs.assertj)
}

application {
    mainClass = "ru.otus.Main"
}

tasks.test {
    useJUnitPlatform()
}
