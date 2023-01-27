package org.taktik.icure.test

import java.net.URI
import com.icure.test.setup.ICureTestSetup
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.security.crypto.password.PasswordEncoder
import org.taktik.couchdb.Client
import org.taktik.icure.asyncdao.GenericDAO
import org.taktik.icure.asyncdao.HealthcarePartyDAO
import org.taktik.icure.asyncdao.InternalDAO
import org.taktik.icure.asyncdao.UserDAO
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
		lateinit var masterHcp: UserCredentials
	}

	@Bean
	@Profile("app")
	fun performStartupTasks(
		userDAO: UserDAO,
		healthcarePartyDAO: HealthcarePartyDAO,
		allDaos: List<GenericDAO<*>>,
		internalDaos: List<InternalDAO<*>>,
		couchDbProperties: CouchDbProperties,
		testGroupProperties: TestProperties,
		passwordEncoder: PasswordEncoder,
	) = ApplicationRunner {

		runBlocking {
			if (testGroupProperties.bootstrapEnv!!) {
				println("Bootstrapping CouchDB Container...")
				bootstrapEnvironment(allDaos, couchDbProperties)
			}

			internalDaos.forEach {
				it.forceInitStandardDesignDocument( true)
			}

			masterHcp = createHcpUser(userDAO, healthcarePartyDAO, passwordEncoder)
		}
	}

	private suspend fun bootstrapEnvironment(allDaos: List<GenericDAO<*>>, couchDbProperties: CouchDbProperties) {
		initStandardDesignDocumentsOf(allDaos)

		ICureTestSetup.bootstrapOss(
			couchDbUrl = couchDbProperties.url,
			couchDbUser = couchDbProperties.username!!,
			couchDbPassword = couchDbProperties.password!!
		)
	}

	private suspend fun initStandardDesignDocumentsOf(allDaos: List<GenericDAO<*>>) {
		allDaos.forEach {
			it.forceInitStandardDesignDocument(true)
		}
	}
}
