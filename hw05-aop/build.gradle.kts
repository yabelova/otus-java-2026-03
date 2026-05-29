import org.gradle.api.tasks.JavaExec
import org.gradle.process.JavaForkOptions

plugins {
    application
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "ru.otus.Demo"
}


tasks.jar {
    manifest {
        attributes("Premain-Class" to "ru.otus.classfileapi.LogAgent")
    }
}

tasks.named<JavaExec>("run") {
    attachAgent()
}

tasks.test {
    useJUnitPlatform()
    attachAgent()
}


private fun Task.attachAgent() {
    val agentJar = tasks.jar.flatMap { it.archiveFile }
    dependsOn("jar")
    doFirst {
        (this as JavaForkOptions).jvmArgs("-javaagent:${agentJar.get().asFile.absolutePath}")
    }
}