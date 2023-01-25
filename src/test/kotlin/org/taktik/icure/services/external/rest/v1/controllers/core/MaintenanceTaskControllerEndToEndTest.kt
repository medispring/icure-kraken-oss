package org.taktik.icure.services.external.rest.v1.controllers.core

import java.net.URI
import java.util.UUID
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.taktik.couchdb.DocIdentifier
import org.taktik.icure.asyncdao.UserDAO
import org.taktik.icure.properties.CouchDbProperties
import org.taktik.icure.services.external.rest.v1.dto.MaintenanceTaskDto
import org.taktik.icure.services.external.rest.v1.dto.PaginatedList
import org.taktik.icure.services.external.rest.v1.dto.embed.DelegationDto
import org.taktik.icure.services.external.rest.v1.dto.embed.TaskStatusDto
import org.taktik.icure.test.ICureTestApplication
import org.taktik.icure.test.UserCredentials
import org.taktik.icure.test.createHttpClient
import org.taktik.icure.test.createPatientUser
import org.taktik.icure.test.makeDeleteRequest
import org.taktik.icure.test.makeGetRequest
import org.taktik.icure.test.makePostRequest
import org.taktik.icure.test.makePutRequest
import org.taktik.icure.test.objectMapper

private const val TEST_CACHE = "build/tests/icureCache"

@SpringBootTest(
	classes = [ICureTestApplication::class],
	properties = [
		"spring.main.allow-bean-definition-overriding=true",
		"icure.objectstorage.icureCloudUrl=test",
		"icure.objectstorage.cacheLocation=$TEST_CACHE",
		"icure.objectstorage.backlogToObjectStorage=true",
		"icure.objectstorage.sizeLimit=1000",
		"icure.objectstorage.migrationDelayMs=1000",
	],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("app")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MaintenanceTaskControllerEndToEndTest(
	passwordEncoder: PasswordEncoder,
	@LocalServerPort val port: Int,
	@Autowired val userDAO: UserDAO,
	@Autowired val couchDbProperties: CouchDbProperties
) : StringSpec({

	val apiUrl = System.getenv("ICURE_URL") ?: "http://localhost"
	val apiVersion: String = System.getenv("API_VERSION") ?: "v1"
	val apiEndpoint = "rest/$apiVersion/maintenancetask"

	val filterTimestamp = System.currentTimeMillis()-10000
	val hcpAuth = "Basic ${java.util.Base64.getEncoder().encodeToString("john:LetMeIn".toByteArray())}"
	var patientCreds: UserCredentials? = null

	fun apiUrl() = if (apiUrl == "http://localhost") "$apiUrl:$port" else apiUrl

	fun maintenanceTaskPostRequest(payload: String, url: String, expectedCode: Int, auth: String): String? {
		return makePostRequest(createHttpClient(auth), url, payload, expectedCode)
	}

	fun getMaintenanceTaskRequest(expectedCode: Int, auth: String, url: String): String? {
		return makeGetRequest(createHttpClient(auth), url, expectedCode)
	}

	fun deleteMaintenanceTaskRequest(expectedCode: Int, auth: String, url: String): String? {
		return makeDeleteRequest(createHttpClient(auth), url, expectedCode)
	}

	fun modifyMaintenanceTaskRequest(expectedCode: Int, auth: String, payload: String): String? {
		return makePutRequest(createHttpClient(auth), "${apiUrl()}/$apiEndpoint", payload, expectedCode)
	}

	beforeTest {
		if (patientCreds == null) {
			patientCreds = createPatientUser(
				createHttpClient(hcpAuth),
				apiUrl(),
				passwordEncoder,
			)
		}
	}

	"Creation of a new MaintenanceTask - Success" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "OTHER",
			status = TaskStatusDto.pending
		)
		val responseBody = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
	}

	"Can get a MaintenanceTask as Hcp" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "OTHER",
			status = TaskStatusDto.pending
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		val responseGet = getMaintenanceTaskRequest( 200, hcpAuth, "${apiUrl()}/$apiEndpoint/${createdTask.id}")
		val retrievedTask = objectMapper.readValue<MaintenanceTaskDto>(responseGet!!)
		retrievedTask.id shouldBe task.id
	}

	"Cannot get a MaintenanceTask as a non delegated Patient" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "OTHER",
			status = TaskStatusDto.pending
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		getMaintenanceTaskRequest( 403, patientCreds!!.auth(), "${apiUrl()}/$apiEndpoint/${createdTask.id}")
	}

	"Can get a Maintenance Task as a delegated Patient" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "OTHER",
			status = TaskStatusDto.pending,
			delegations = mapOf(patientCreds!!.dataOwnerId to setOf(DelegationDto()))
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		val responseGet = getMaintenanceTaskRequest( 200, patientCreds!!.auth(), "${apiUrl()}/$apiEndpoint/${createdTask.id}")
		val retrievedTask = objectMapper.readValue<MaintenanceTaskDto>(responseGet!!)
		retrievedTask.id shouldBe task.id
	}

	"Can delete a MaintenanceTask as Hcp" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "OTHER",
			status = TaskStatusDto.pending
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		val responseGet = deleteMaintenanceTaskRequest( 200, hcpAuth, "${apiUrl()}/$apiEndpoint/${createdTask.id}")
		val retrievedTasksId = objectMapper.readValue<List<DocIdentifier>>(responseGet!!)
		retrievedTasksId.size shouldBe 1
		retrievedTasksId.first().id shouldNotBe null
		retrievedTasksId.first().id shouldBe task.id
	}

	"Cannot delete a MaintenanceTask as a non delegated Patient" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "OTHER",
			status = TaskStatusDto.pending
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		deleteMaintenanceTaskRequest( 403, patientCreds!!.auth(), "${apiUrl()}/$apiEndpoint/${createdTask.id}")
	}

	"Can delete a Maintenance Task as a delegated Patient" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			rev = "DEFINITELY_NOT_NULL",
			taskType = "OTHER",
			status = TaskStatusDto.pending,
			delegations = mapOf(patientCreds!!.dataOwnerId to setOf(DelegationDto()))
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		val responseGet = deleteMaintenanceTaskRequest( 200, patientCreds!!.auth(), "${apiUrl()}/$apiEndpoint/${createdTask.id}")
		val retrievedTasksId = objectMapper.readValue<List<DocIdentifier>>(responseGet!!)
		retrievedTasksId.size shouldBe 1
		retrievedTasksId.first().id shouldNotBe null
		retrievedTasksId.first().id shouldBe task.id
	}

	"Can modify a MaintenanceTask as Hcp" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "TYPE-1",
			status = TaskStatusDto.pending
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		createdTask.taskType shouldBe "TYPE-1"
		val responseGet = modifyMaintenanceTaskRequest( 200, hcpAuth, objectMapper.writeValueAsString(createdTask.copy(taskType = "TYPE-2")))
		val retrievedTask = objectMapper.readValue<MaintenanceTaskDto>(responseGet!!)
		retrievedTask.id shouldBe task.id
		retrievedTask.taskType shouldBe "TYPE-2"
	}

	"Cannot modify a MaintenanceTask as a non delegated Patient" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "TYPE-1",
			status = TaskStatusDto.pending
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		modifyMaintenanceTaskRequest( 403, patientCreds!!.auth(), objectMapper.writeValueAsString(createdTask.copy(taskType = "TYPE-2")))
	}

	"Can modify a Maintenance Task as a delegated Patient" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "TYPE-1",
			status = TaskStatusDto.pending,
			delegations = mapOf(patientCreds!!.dataOwnerId to setOf(DelegationDto()))
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		createdTask.taskType shouldBe "TYPE-1"
		val responseGet = modifyMaintenanceTaskRequest( 200, patientCreds!!.auth(), objectMapper.writeValueAsString(createdTask.copy(taskType = "TYPE-2")))
		val retrievedTask = objectMapper.readValue<MaintenanceTaskDto>(responseGet!!)
		retrievedTask.id shouldBe task.id
		retrievedTask.taskType shouldBe "TYPE-2"
	}

	"Can filter a MaintenanceTask as Hcp" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "OTHER",
			status = TaskStatusDto.pending,
			delegations = mapOf(ICureTestApplication.masterHcp!!.dataOwnerId to setOf(DelegationDto()))
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		val responseGet = maintenanceTaskPostRequest(
			"{\"\$type\":\"FilterChain\",\"filter\":{\"date\":${filterTimestamp},\"\$type\":\"MaintenanceTaskAfterDateFilter\"}}",
			"${apiUrl()}/$apiEndpoint/filter", 200, hcpAuth)
		val retrievedTasks = objectMapper.readValue<PaginatedList<MaintenanceTaskDto>>(responseGet!!)
		retrievedTasks.rows.size shouldBeGreaterThan 0
		retrievedTasks.rows.forEach {
			it.created shouldNotBe null
			it.created!! shouldBeGreaterThanOrEqual filterTimestamp
		}
	}

// 	"Cannot filter a MaintenanceTask as a non delegated Patient" {
// 		val responseGet = maintenanceTaskPostRequest(
// 			"{\"\$type\":\"FilterChain\",\"filter\":{\"date\":${filterTimestamp},\"\$type\":\"MaintenanceTaskAfterDateFilter\"}}",
// 			"${apiUrl()}/$apiEndpoint/filter", 200, patientCreds!!.auth())
// 		val retrievedTasks = objectMapper.readValue<PaginatedList<MaintenanceTaskDto>>(responseGet!!)
// 		retrievedTasks.rows.size shouldBe 0
// 	}

	"Can filter a Maintenance Task as a delegated Patient" {
		val task = MaintenanceTaskDto(
			id = UUID.randomUUID().toString(),
			taskType = "TYPE-1",
			status = TaskStatusDto.pending,
			delegations = mapOf(patientCreds!!.dataOwnerId to setOf(DelegationDto()))
		)
		val responsePost = maintenanceTaskPostRequest(objectMapper.writeValueAsString(task), "${apiUrl()}/$apiEndpoint", 200, hcpAuth)
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responsePost!!)
		createdTask.id shouldBe task.id
		createdTask.taskType shouldBe "TYPE-1"
		val responseGet = maintenanceTaskPostRequest(
			"{\"\$type\":\"FilterChain\",\"filter\":{\"ids\":[\"${task.id}\"],\"\$type\":\"MaintenanceTaskByIdsFilter\"}}",
			"${apiUrl()}/$apiEndpoint/filter", 200, hcpAuth)
		val retrievedTasks = objectMapper.readValue<PaginatedList<MaintenanceTaskDto>>(responseGet!!)
		retrievedTasks.rows.size shouldBe 1
		retrievedTasks.rows.first().id shouldBe task.id
	}

})
