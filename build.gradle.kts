/*
 *    Copyright 2020 Taktik SA
 */

import java.text.SimpleDateFormat
import java.util.Date
import com.google.devtools.ksp.gradle.KspTask
import org.icure.task.CleanCouchDockerTask
import org.icure.task.StartCouchDockerTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun



val ktlint by configurations.creating

val repoUsername: String by project
val repoPassword: String by project
val mavenReleasesRepository: String by project
val kmapVersion = "0.1.52-main.8d4a565b58"

plugins {
    kotlin("jvm") version "1.7.20"
    id("org.sonarqube") version "3.3"
    id("com.google.devtools.ksp") version "1.7.20-1.0.6"
    `maven-publish`
}

sonarqube {
    properties {
        property("sonar.projectKey", "icure-io_icure-kotlin-sdk")
        property("sonar.organization", "icure-io")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

buildscript {
    repositories {
      mavenCentral()
      gradlePluginPortal()
      maven { url = uri("https://maven.taktik.be/content/groups/public") }
      maven { url = uri("https://repo.spring.io/plugins-release") }
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.5.13")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.7.20")
        classpath("com.taktik.gradle:gradle-plugin-docker-java:2.1.4")
        classpath("com.taktik.gradle:gradle-plugin-git-version:2.0.4")
    }
}

apply(plugin = "git-version")

val gitVersion: String? by project

group = "com.icure"
version = gitVersion ?: "0.0.1-SNAPSHOT"

apply(plugin = "kotlin-spring")
apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "docker-java")

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven { url = uri("https://maven.taktik.be/content/groups/public") }
    maven { url = uri("https://www.e-contract.be/maven2/") }
    maven { url = uri("https://repo.ehealth.fgov.be/artifactory/maven2/") }
}

apply(plugin = "kotlin")
apply(plugin = "maven-publish")

tasks.register("KspPreCheck") {
	inputs.dir("src/main/kotlin/org/taktik/icure/domain")
	inputs.dir("src/main/kotlin/org/taktik/icure/dto")
	inputs.dir("src/main/kotlin/org/taktik/icure/entities")
	inputs.dir("src/main/kotlin/org/taktik/icure/services/external/rest/v1/dto")
	inputs.dir("src/main/kotlin/org/taktik/icure/services/external/rest/v1/mapper")
	inputs.dir("src/main/kotlin/org/taktik/icure/services/external/rest/v2/dto")
	inputs.dir("src/main/kotlin/org/taktik/icure/services/external/rest/v2/mapper")
	outputs.dir("build/generated/ksp/main/kotlin")
	doLast {
		println("Checking for modifications in mappers")
	}
}

tasks.withType<KspTask> {
	dependsOn("KspPreCheck")
}

// KSP doesn't like incremental compiling
// If the KspPreCheck task is-up-to date, then it means that the annotated files were not modified
// So the KSP can be disabled
gradle.taskGraph.whenReady {

	gradle.taskGraph.beforeTask {
		if (this.name == "kspKotlin") {
			this.enabled = tasks.asMap["KspPreCheck"]?.state?.upToDate?.not() ?: true
		}
	}

}

tasks.withType<Test> {
    useJUnitPlatform()
	/*
	 * If tests rely on system properties we can use gradle settings to set the properties but then we need to propagate them to the actual test virtual machine.
	 * This example propagates all properties passed to gradle, it would be better to only propagate the necessary properties.
	 */
	// System.getProperties().forEach { k,	v -> systemProperty(k as String, v) }
}

tasks.withType<JavaCompile> {
    options.isFork = true
    options.fork("memoryMaximumSize" to "4096m")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<PublishToMavenRepository> {
    doFirst {
        println("Artifact >>> ${project.group}:${project.name}:${project.version} <<< published to Maven repository")
    }
}

tasks.withType<BootJar> {
    mainClass.set("org.taktik.icure.ICureBackendApplicationKt")
    manifest {
        attributes(mapOf(
            "Built-By"        to System.getProperties()["user.name"],
            "Build-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Date()),
            "Build-Revision"  to gitVersion,
            "Created-By"      to "Gradle ${gradle.gradleVersion}",
            "Build-Jdk"       to "${System.getProperties()["java.version"]} (${System.getProperties()["java.vendor"]} ${System.getProperties()["java.vm.version"]})",
            "Build-OS"        to "${System.getProperties()["os.name"]} ${System.getProperties()["os.arch"]} ${System.getProperties()["os.version"]}"
        ))
    }
}

tasks.withType<BootRun> {
    if ( project.hasProperty("jvmArgs") ) {
        jvmArgs = (project.getProperties()["jvmArgs"] as String).split(Regex("\\s+"))
    }
}

configure<com.taktik.gradle.plugins.flowr.DockerJavaPluginExtension> {
	imageRepoAndName = "taktik/kraken"
}

configurations {
    all {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
        exclude(group = "log4j", module = "log4j")
    }
    listOf(apiElements, runtimeElements).forEach {
        it.get().outgoing.artifacts.removeIf {
            it.buildDependencies.getDependencies(null).any { it is Jar }
        }
        it.get().outgoing.artifact(tasks.withType<BootJar>().first())
    }
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir("build/generated/ksp/main/kotlin")
        }
    }
}


dependencies {
    implementation(group = "io.icure", name = "kmap", version = kmapVersion)
    ksp(group = "io.icure", name = "kmap", version = kmapVersion)

    implementation(group = "io.projectreactor", name = "reactor-core", version = "3.4.17")
    implementation(group = "io.projectreactor", name = "reactor-tools", version = "3.4.17")
    implementation(group = "io.projectreactor.netty", name = "reactor-netty", version = "1.0.24")

    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.7.20")
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-reflect", version = "1.7.20")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.6.4")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core-jvm", version = "1.6.4")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-reactive", version = "1.6.4")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-reactor", version = "1.6.4")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-collections-immutable-jvm", version = "0.3.5")

    //Jackson
	implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.12.7")
	implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.12.7")
    implementation(group = "com.fasterxml.jackson.datatype", name="jackson-datatype-jsr310", version = "2.12.7")
    implementation(group = "org.mapstruct", name = "mapstruct", version = "1.3.1.Final")

    //Krouch
    implementation(group = "org.taktik.couchdb", name = "krouch", version = "1.1.13-g13e4fcb88e")
    implementation(group = "io.icure", name = "async-jackson-http-client", version = "0.2.6-g02c59d08a5")
    implementation(group = "io.icure", name = "mapper-processor", version = "0.1.1-32d45af2a6")

    implementation(group = "org.springframework.boot", name = "spring-boot-starter-mail", version = "2.5.5")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-webflux", version = "2.5.14")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-security", version = "2.5.14")
	implementation(group = "org.springframework.boot", name= "spring-boot-starter-cache", version = "2.5.14")


	implementation(group = "org.springframework", name = "spring-aspects", version = "5.3.10")
    implementation(group = "org.springframework", name = "spring-websocket", version = "5.3.10")

    implementation(group = "org.springframework.session", name = "spring-session-core", version = "2.5.2")

    implementation(group = "org.hibernate.validator", name = "hibernate-validator", version = "6.1.5.Final")
    implementation(group = "org.hibernate.validator", name = "hibernate-validator-annotation-processor", version = "6.1.5.Final")
    implementation(group = "org.hibernate.validator", name = "hibernate-validator-cdi", version = "6.1.5.Final")

    implementation(group = "com.github.ben-manes.caffeine", name = "caffeine", version = "3.0.6")

    // Logging
    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.11")
    implementation(group = "ch.qos.logback", name = "logback-access", version = "1.2.11")

    implementation(group = "org.slf4j", name = "slf4j-api", version = "1.7.36")
    implementation(group = "org.slf4j", name = "jul-to-slf4j", version = "1.7.32")
    implementation(group = "org.slf4j", name = "jcl-over-slf4j", version = "1.7.32")
    implementation(group = "org.slf4j", name = "log4j-over-slf4j", version = "1.7.36")

    // Java Commons
    implementation(group = "org.taktik.commons", name = "commons-uti", version = "1.0")

    // APIs
    implementation(group = "javax.servlet", name = "javax.servlet-api", version = "3.1.0")
    implementation(group = "javax.annotation", name = "jsr250-api", version = "1.0")
    implementation(group = "javax.activation", name = "activation", version = "1.1.1")
    implementation(group = "javax.xml.bind", name = "jaxb-api", version = "2.3.1")
    implementation(group = "javax.el", name = "javax.el-api", version = "3.0.0")

    implementation(group = "org.glassfish.jaxb", name = "jaxb-runtime", version = "2.3.1")
    implementation(group = "org.glassfish", name = "javax.el", version = "3.0.1-b12")
    implementation(group = "org.reflections", name = "reflections", version = "0.9.12")

    // Commons
    implementation(group = "com.google.guava", name = "guava", version = "31.1-jre")
    implementation(group = "commons-io", name = "commons-io", version = "2.11.0")
    implementation(group = "org.apache.commons", name = "commons-lang3", version = "3.12.0")
    implementation(group = "org.apache.commons", name = "commons-compress", version = "1.21")
    implementation(group = "org.apache.commons", name = "commons-math3", version = "3.6.1")
    implementation(group = "commons-beanutils", name = "commons-beanutils", version = "1.9.4")

    // Bouncy Castle
    implementation(group = "org.bouncycastle", name = "bcprov-jdk15on", version = "1.70")
    implementation(group = "org.bouncycastle", name = "bcmail-jdk15on", version = "1.70")

    //2FA
    implementation(group = "org.jboss.aerogear", name = "aerogear-otp-java", version = "1.0.0")

    // Swagger
    implementation(group = "org.springdoc", name = "springdoc-openapi-webflux-ui", version = "1.6.7")
    implementation(group = "org.springdoc", name = "springdoc-openapi-kotlin", version = "1.5.13")

    //Saxon
    implementation(group = "net.sf.saxon", name = "Saxon-HE", version = "9.6.0-6")

    //EH Validator
    implementation(group = "com.ibm.icu", name = "icu4j", version = "57.1")
    implementation(files("libs/ehvalidator-service-core-2.1.1.jar"))

    // Mustache
    implementation(group = "com.github.spullara.mustache.java", name = "compiler", version = "0.9.10")

    //Sendgrid
    implementation(group = "com.sendgrid", name = "sendgrid-java", version = "4.4.7")

	// JWT
	implementation(group = "io.jsonwebtoken", name = "jjwt-api", version = "0.11.5")
	implementation(group = "io.jsonwebtoken", name = "jjwt-impl", version = "0.11.5")
	implementation(group = "io.jsonwebtoken", name = "jjwt-jackson", version = "0.11.5")

	//Apple Silicon Compatibility
	implementation("io.netty:netty-resolver-dns-native-macos:4.1.72.Final:osx-aarch_64")

	//RRules
	implementation(group = "org.dmfs", name = "lib-recur", version = "0.13.0")

	ktlint("com.pinterest:ktlint:0.45.2") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
    // additional 3rd party ruleset(s) can be specified here
    // just add them to the classpath (e.g. ktlint 'groupId:artifactId:version') and
    // ktlint will pick them up

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.4.2")
    testImplementation(group = "org.springframework.boot", name = "spring-boot-starter-test", version = "2.5.14")
    testImplementation(group = "io.mockk", name = "mockk", version = "1.11.0")
    testImplementation(group = "com.ninja-squad", name = "springmockk", version = "3.1.1")
	testImplementation(group = "io.kotest", name = "kotest-assertions-core", version = "4.4.3")
	testImplementation(group = "io.kotest", name = "kotest-runner-junit5", version = "4.4.3")
	testImplementation(group = "io.kotest.extensions", name = "kotest-extensions-spring", version = "1.0.1")
	testImplementation(group = "io.icure", name = "icure-e2e-test-setup", version = "0.0.7-gfed922635a")
}

tasks.withType<Test> {
	minHeapSize = "512m"
	maxHeapSize = "2048m"
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)
    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("src/**/*.kt")
}

val ktlintFiles by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    val split = if (project.hasProperty("inputFiles")) project.property("inputFiles")?.toString()?.split(',') ?: emptyList() else emptyList()

    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = split
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("-F", "src/**/*.kt")
}

val setupKtlintPreCommitHook by tasks.creating(Exec::class) {
	try {
		exec {
			workingDir = File("${rootProject.projectDir}")
			commandLine = listOf("sh", "-c", "git config --local include.path ./.gitconfig > /dev/null 2>&1 || true")
		}
	} catch (e: Throwable) {
		println("Cannot install KtLint pre-commit hook, please execute ktlint format by hand before committing")
	}
}

publishing {
    publications {
        create<MavenPublication>("icure-oss") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "Taktik"
            url = uri(mavenReleasesRepository)
            credentials {
                username = repoUsername
                password = repoPassword
            }
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.isIncremental = true
}


// Those two tasks require Kotlin 1.7 to run properly: Do not forget to update your project settings
val cleanCouchTask = tasks.register<CleanCouchDockerTask>("cleanCouchTask")
val startCouchTask = tasks.register<StartCouchDockerTask>("startCouchTask")

tasks.getByName("test") {
	dependsOn(startCouchTask)
	finalizedBy(cleanCouchTask)
}
