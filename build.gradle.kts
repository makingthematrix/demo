plugins {
    scala
    id("io.micronaut.application") version "5.0.2"
    id("com.gradleup.shadow") version "9.4.1"
}

version = "0.1"
group = "com.example"

val scalaVersion = "3.9.0"
val micronautScalaVersion = "1.0.0-M1"
val javaTarget = 25

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.scala-lang:scala3-library_3:$scalaVersion")
    scalaCompilerPlugins("io.micronaut.scala:micronaut-inject-scala_$scalaVersion:$micronautScalaVersion")
    runtimeOnly("io.micronaut.scala:micronaut-runtime-scala:$micronautScalaVersion")

    implementation("io.micronaut.serde:micronaut-serde-jackson")
    compileOnly("io.micronaut:micronaut-http-client")
    runtimeOnly("ch.qos.logback:logback-classic")
    testImplementation("io.micronaut:micronaut-http-client")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "com.example.Application"
}

java {
    sourceCompatibility = JavaVersion.toVersion(javaTarget)
    targetCompatibility = JavaVersion.toVersion(javaTarget)
}

tasks.withType<ScalaCompile>().configureEach {
    scalaCompileOptions.additionalParameters.addAll(
        listOf("-release:$javaTarget", "-J--add-modules=java.compiler", "-no-indent")
    )
    scalaCompileOptions.forkOptions.jvmArgs =
        (scalaCompileOptions.forkOptions.jvmArgs ?: emptyList()) + "--add-modules=java.compiler"
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
}

// https://docs.gradle.org/current/userguide/upgrading_major_version_9.html#test_task_fails_when_no_tests_are_discovered
tasks.withType<AbstractTestTask>().configureEach {
    failOnNoDiscoveredTests = false
}
