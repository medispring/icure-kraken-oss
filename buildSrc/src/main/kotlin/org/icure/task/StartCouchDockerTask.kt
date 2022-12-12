package org.icure.task

import java.io.File
import java.security.MessageDigest
import com.icure.test.setup.ICureTestSetup
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class StartCouchDockerTask : DefaultTask() {

	@TaskAction
	fun startDocker() {
		runBlocking {
			val testProperties = File("src/test/resources/icure-test.properties").readLines()
				.filter { it.contains("icure.couchdb") || it.contains("icure.test.group") }
				.map {it.split('=') }
				.map { it.first() to it.last() }
				.toMap()

			val couchDbUrl = testProperties.get("icure.couchdb.url") ?: throw RuntimeException("Impossible to start a CouchDB Container: Missing icure.couchdb.url property")

			if (!couchDbUrl.contains("localhost") && !couchDbUrl.contains("127.0.0.1")) {
				println("Not starting CouchDB Container: Provided icure.couchdb.url is not a local URL")
			} else {
				val couchDbUsername = testProperties.get("icure.couchdb.username") ?: throw RuntimeException("Impossible to start a CouchDB Container: Missing icure.couchdb.username property")
				val couchDbPassword = testProperties.get("icure.couchdb.password") ?: throw RuntimeException("Impossible to start a CouchDB Container: Missing icure.couchdb.password property")
				val couchDbLocalPort = couchDbUrl.substringAfterLast(':')

				println("Starting CouchDB Container for our test session...")
				val dockerName = ICureTestSetup.startCouchDbContainer(couchDbLocalPort, couchDbUsername, couchDbPassword)

				File("buildSrc/generated/couchdb_docker.json").writeText(dockerName)
				println("CouchDB Container $dockerName up and running")
			}
		}
	}
}
