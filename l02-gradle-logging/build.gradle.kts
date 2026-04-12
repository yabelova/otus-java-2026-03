plugins {
    application
}

dependencies {
    implementation(project(":l02-lib"))
    implementation(libs.slf4j)
    runtimeOnly(libs.logback)
    runtimeOnly(libs.logstash)
}

application {
    mainClass = "ru.otus.Main"
}