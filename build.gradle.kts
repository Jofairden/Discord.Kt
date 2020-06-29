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
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

subprojects {
    // Gradle plugin that automatically creates check and format tasks for project Kotlin sources, supports different kotlin plugins and Gradle build caching.
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        debug.set(true)
    }
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${gradleProperty("coroutines-version")}")
    implementation("com.squareup.okhttp3:okhttp:${gradleProperty("okhttp-version")}")
    implementation("com.fasterxml.jackson.core:jackson-core:${gradleProperty("jackson-version")}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${gradleProperty("jackson-version")}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${gradleProperty("jackson-version")}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${gradleProperty("jackson-version")}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${gradleProperty("jackson-version")}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${gradleProperty("jackson-version")}")
    implementation("com.squareup.retrofit2:retrofit:${gradleProperty("retrofit-version")}")
    implementation("com.squareup.retrofit2:converter-jackson:${gradleProperty("retrofit-version")}")
    implementation("io.github.microutils:kotlin-logging:${gradleProperty("kotlin-logging-version")}")
    implementation("org.slf4j:slf4j-simple:${gradleProperty("sl4j-simple-version")}")
    implementation("com.github.ben-manes.caffeine:caffeine:${gradleProperty("caffeine-version")}")
    implementation("org.koin:koin-core:${gradleProperty("koin-version")}")
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

publishing {
    repositories {
        maven {
            /** We publish here */
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jofairden/discord.kt")
            /** Coming from Github */
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("discord.kt") {
            from(components["java"])
        }
    }
    /** See gradle.properties for group and version */
}

/**
 * Get a property from the gradle.properties
 */
fun gradleProperty(key: String): String {
    return gradle.rootProject.extra[key]?.toString() ?: return "NULL"
}
