package org.taktik.icure.test

import java.net.URI
import java.util.UUID
import com.icure.test.setup.ICureTestSetup
import com.icure.test.setup.ICureTestSetup.MasterHcpCredentials
import kotlinx.coroutines.runBlocking
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.taktik.couchdb.Client
import org.taktik.icure.asyncdao.GenericDAO
import org.taktik.icure.asyncdao.InternalDAO
import org.taktik.icure.asyncdao.impl.CouchDbDispatcher
import org.taktik.icure.properties.CouchDbProperties

@SpringBootApplication(
	scanBasePackages = [
		"org.springframework.boot.autoconfigure.aop",
		"org.springframework.boot.autoconfigure.context",
		"org.springframework.boot.autoconfigure.validation",
		"org.springframework.boot.autoconfigure.websocket",
		"org.taktik.icure.config",
		"org.taktik.icure.asyncdao",
		"org.taktik.icure.asynclogic",
		"org.taktik.icure.be.ehealth.logic",
		"org.taktik.icure.be.format.logic",
		"org.taktik.icure.properties",
		"org.taktik.icure.db",
		"org.taktik.icure.dao",
		"org.taktik.icure.services.external.http",
		"org.taktik.icure.services.external.rest.v1.controllers",
		"org.taktik.icure.services.external.rest.v1.mapper",
		"org.taktik.icure.services.external.rest.v1.wscontrollers",
		"org.taktik.icure.services.external.rest.v2.controllers",
		"org.taktik.icure.services.external.rest.v2.mapper",
		"org.taktik.icure.services.external.rest.v2.wscontrollers",
		"org.taktik.icure.errors",
		"org.taktik.icure.test"
	],
	exclude = [
		FreeMarkerAutoConfiguration::class,
		CacheAutoConfiguration::class,
		DataSourceAutoConfiguration::class,
		JndiDataSourceAutoConfiguration::class,
		ErrorWebFluxAutoConfiguration::class
	]
)
@PropertySource("classpath:icure-test.properties")
@TestConfiguration
class ICureTestApplication {

	companion object {
		var masterHcp: MasterHcpCredentials? = null
	}

	@Bean
	@Profile("app")
	fun performStartupTasks(
		allDaos: List<GenericDAO<*>>,
		internalDaos: List<InternalDAO<*>>,
		couchDbProperties: CouchDbProperties,
		testGroupProperties: TestProperties,
		@Qualifier("baseCouchDbDispatcher") couchDbDispatcher: CouchDbDispatcher) = ApplicationRunner {

		runBlocking {
			if (testGroupProperties.bootstrapEnv!!) {
				println("Bootstrapping CouchDB Container...")
				bootstrapEnvironment(allDaos, couchDbDispatcher, couchDbProperties, testGroupProperties)
			}

			if (testGroupProperties.groupId != null) {
				println("Initializing existing groupId ${testGroupProperties.groupId}...")
				val couchClient = couchDbDispatcher.getClient(URI.create(couchDbProperties.url), testGroupProperties.groupId)
				initStandardDesignDocumentsOf(allDaos, couchClient)
			} else {
				val groupId = "ic-e2e-${UUID.randomUUID()}"
				ICureTestSetup.createGroup(groupId, UUID.randomUUID().toString(), couchDbProperties.url, couchDbProperties.username!!, couchDbProperties.password!!)
				val couchClient = couchDbDispatcher.getClient(URI.create(couchDbProperties.url), groupId)
				initStandardDesignDocumentsOf(allDaos, couchClient)

				masterHcp = ICureTestSetup.createMasterHcpIn(groupId, couchDbProperties.url, couchDbProperties.username!!, couchDbProperties.password!!)
			}

			internalDaos.forEach {
				it.forceInitStandardDesignDocument(true)
			}
		}
	}

	private suspend fun bootstrapEnvironment(allDaos: List<GenericDAO<*>>, couchDbDispatcher: CouchDbDispatcher, couchDbProperties: CouchDbProperties, testGroupProperties: TestProperties) {
		val couchDbClient = couchDbDispatcher.getClient(URI.create(couchDbProperties.url))
		initStandardDesignDocumentsOf(allDaos, couchDbClient)

		ICureTestSetup.bootstrapCloud(
			testGroupProperties.adminGroupId!!,
			testGroupProperties.adminGroupPassword!!,
			groupUserLogin = testGroupProperties.adminUsername!!,
			groupUserPasswordHash = DigestUtils.sha256Hex(testGroupProperties.adminPassword!!),
			couchDbUrl = couchDbProperties.url,
			couchDbUser = couchDbProperties.username!!,
			couchDbPassword = couchDbProperties.password!!
		)

		val couchDbClientForAdminGroup = couchDbDispatcher.getClient(URI.create(couchDbProperties.url), "xx")
		initStandardDesignDocumentsOf(allDaos, couchDbClientForAdminGroup)
	}

	private suspend fun initStandardDesignDocumentsOf(allDaos: List<GenericDAO<*>>, couchClient: Client) {
		allDaos.forEach {
			it.forceInitStandardDesignDocument(couchClient, true)
		}
	}
}
