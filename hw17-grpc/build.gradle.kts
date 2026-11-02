import com.google.protobuf.gradle.*

plugins {
    application
    alias(libs.plugins.protobuf)
}

dependencies {
    implementation(libs.grpc.netty)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.stub)
    implementation(libs.protobuf.java)
    implementation(libs.slf4j)
    runtimeOnly(libs.logback)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

application {
    mainClass = "ru.otus.grpc.GRPCClient"
}

val grpcVersion = libs.grpc.stub.get().version
val protobufVersion = libs.protobuf.java.get().version

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
    }

    generateProtoTasks {
        all().configureEach {
            plugins {
                id("grpc")
            }
        }
    }
}

tasks.register<JavaExec>("runServer") {
    group = "application"
    description = "Starts the gRPC example server."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "ru.otus.grpc.GRPCServer"
}

tasks.test {
    useJUnitPlatform()
}
