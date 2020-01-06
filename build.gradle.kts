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
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
}

tasks {
	compileKotlin {
		kotlinOptions.jvmTarget = gradleProperty("jvm-target")
		kotlinOptions.freeCompilerArgs = listOf("-Xsjr305=strict")
		incremental = true
	}
	compileTestKotlin {
		kotlinOptions.jvmTarget = gradleProperty("jvm-target")
		kotlinOptions.freeCompilerArgs = listOf("-Xsjr305=strict")
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
fun gradleProperty(key : String) : String {
	return gradle.rootProject.extra[key]?.toString() ?: return "NULL"
}