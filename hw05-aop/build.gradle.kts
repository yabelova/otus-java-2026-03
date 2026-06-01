import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.testing.Test
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
    mainClass = "ru.otus.demo.ProxyDemo"
}

val runAgent by tasks.registering(JavaExec::class) {
    group = "application"
    description = "Run AgentDemo with javaagent"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "ru.otus.demo.AgentDemo"
    attachAgent()
}

tasks.jar {
    manifest {
        attributes("Premain-Class" to "ru.otus.classfileapi.LogAgent")
    }
}

tasks.test {
    useJUnitPlatform {
        excludeTags("agent")
    }
}

val testAgent by tasks.registering(Test::class) {
    group = "verification"
    description = "Runs agent tests with javaagent"
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform {
        includeTags("agent")
    }
    attachAgent()
}

private fun Task.attachAgent() {
    val agentJar = tasks.jar.flatMap { it.archiveFile }
    dependsOn("jar")
    doFirst {
        (this as JavaForkOptions).jvmArgs("-javaagent:${agentJar.get().asFile.absolutePath}")
    }
}