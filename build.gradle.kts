
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
	}
	compileTestKotlin {
		kotlinOptions.jvmTarget = gradleProperty("jvm-target")
	}
}

/**
 * Get a property from the gradle.properties
 */
fun gradleProperty(key : String) : String {
	return gradle.rootProject.extra[key]?.toString() ?: return "NULL"
}