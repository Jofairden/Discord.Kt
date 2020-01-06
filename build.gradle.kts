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
    jcenter()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${gradleProperty("coroutines-version")}")
    implementation(fuel("fuel", gradleProperty("fuel-version")))
    implementation(fuel("fuel-coroutines", gradleProperty("fuel-version")))
    implementation("com.squareup.okhttp3:okhttp:${gradleProperty("okhttp-version")}")
    implementation("com.fasterxml.jackson.core:jackson-core:${gradleProperty("jackson-version")}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${gradleProperty("jackson-version")}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${gradleProperty("jackson-version")}")
    compile("io.github.microutils:kotlin-logging:${gradleProperty("kotlin-logging-version")}")
    compile("org.slf4j:slf4j-simple:${gradleProperty("sl4j-simple-version")}")
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

fun DependencyHandler.fuel(module: String, version: String? = null): Any =
    "com.github.kittinunf.fuel:$module${version?.let { ":$version" } ?: ""}"
