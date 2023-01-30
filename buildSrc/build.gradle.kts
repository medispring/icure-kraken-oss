plugins {
	id("org.jetbrains.kotlin.jvm") version "1.7.20"

}
version = "0.0.1-SNAPSHOT"

val iCureE2eTestVersion = "0.0.1-geb49419b5a"

repositories {
	mavenCentral()
	maven("https://jitpack.io")
	maven { url = uri("https://maven.taktik.be/content/groups/public") }
}

dependencies {

	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.google.guava:guava:31.0.1-jre")
	implementation("com.github.kotlinx.ast:grammar-kotlin-parser-antlr-kotlin:0.1.0")
	implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.12.6")
	implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.12.6")

	implementation(group = "io.icure", name = "icure-e2e-test-setup", version = iCureE2eTestVersion)
	implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.6.1")
}
