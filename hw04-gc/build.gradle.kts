import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.jmh)
}

dependencies {
    implementation(libs.slf4j)
    runtimeOnly(libs.logback)

    "jmhImplementation"(libs.jmh.core)
    "jmhAnnotationProcessor"(libs.jmh.annprocess)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

jmh {
    includeTests = false

    val heap = project.findProperty("heap")?.toString() ?: "256m"
    val gcType = project.findProperty("gc")?.toString() ?: "UseG1GC"

    val timestamp = SimpleDateFormat("MM-dd_HH-mm").format(Date())

    jvmArgs.addAll(
        listOf(
            "-Xmx$heap",
            "-Xms$heap",
            "-XX:+$gcType"
        )
    )

    warmupIterations = 2
    iterations = 5
    fork = 1

    profilers.add("gc")

    resultFormat.set("text")
    resultsFile.set(project.file("build/results/jmh/results_${gcType}_${heap}_${timestamp}.txt"))
}

tasks.test {
    useJUnitPlatform()
}

