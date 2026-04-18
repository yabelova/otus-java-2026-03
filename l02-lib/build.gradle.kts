dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(platform(libs.mockito.bom))

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.bundles.mockito.api)

    testRuntimeOnly(libs.junit.launcher)
}

tasks.test {
    useJUnitPlatform()
}