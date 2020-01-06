require(hasProperty("project-group"))
require(hasProperty("project-version"))
require(hasProperty("project-name"))
require(hasProperty("jvm-target"))

group = gradleProperty("project-group")
version = gradleProperty("project-version")
val projectName = gradleProperty("project-name")
val jvmTarget = gradleProperty("jvm-target")

plugins {
    kotlin("jvm") version "1.3.61"
    id("org.jlleitschuh.gradle.ktlint") version "9.1.1"
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    compile("io.github.microutils:kotlin-logging:1.7.7")
    compile("org.slf4j:slf4j-simple:1.7.26")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = gradleProperty("jvm-target")
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
        incremental = true
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = gradleProperty("jvm-target")
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
        incremental = true
    }
    compileJava {
        options.isIncremental = true
        options.isFork = true
        sourceCompatibility = gradleProperty("jvm-target")
        targetCompatibility = gradleProperty("jvm-target")
    }
}

/**
 * Get a property from the gradle.properties
 */
fun gradleProperty(key: String): String {
    return gradle.rootProject.extra[key]?.toString() ?: return "NULL"
}
