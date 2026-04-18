dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.bundles.junit.api)
    testRuntimeOnly(libs.junit.launcher)
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("testSmoke") {
    group = "verification"
    description = "Runs only smoke tests"

    testClassesDirs = tasks.test.get().testClassesDirs
    classpath = tasks.test.get().classpath

    useJUnitPlatform {
        includeTags("smoke")
    }
}