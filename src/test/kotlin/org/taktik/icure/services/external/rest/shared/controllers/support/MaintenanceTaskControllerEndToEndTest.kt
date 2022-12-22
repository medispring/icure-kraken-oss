package org.taktik.icure.services.external.rest.shared.controllers.support

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
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
import org.taktik.icure.test.*
import java.net.URI
import java.util.*
import io.kotest.core.script.test
import kotlinx.coroutines.runBlocking

@SpringBootTest(
	classes = [ICureTestApplication::class],
	properties = [
		"spring.main.allow-bean-definition-overriding=true",
		"icure.objectstorage.icureCloudUrl=test",
		"icure.objectstorage.cacheLocation=build/tests/icureCache",
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
) : StringSpec() {

	val apiUrl = System.getenv("ICURE_URL") ?: "http://localhost"

	val filterTimestamp = System.currentTimeMillis() - 10000
	val hcpAuth = "Basic ${Base64.getEncoder().encodeToString("${ICureTestApplication.masterHcp!!.login}:${ICureTestApplication.masterHcp!!.password}".toByteArray())}"

	init {
		runBlocking {
			val patientCredentials = createPatientUser(
				createHttpClient(hcpAuth),
				"$apiUrl:$port",
				ICureTestApplication.masterHcp!!.groupId,
				passwordEncoder,
				userDAO,
				URI.create(couchDbProperties.url)
			)
			testMaintenanceTask(
				"$apiUrl:$port",
				"v1",
				"rest/v1/maintenancetask",
				hcpAuth,
				patientCredentials,
				filterTimestamp
			)
			testMaintenanceTask(
				"$apiUrl:$port",
				"v2",
				"rest/v2/maintenancetask",
				hcpAuth,
				patientCredentials,
				filterTimestamp
			)
		}
	}
}

fun StringSpec.testMaintenanceTask(
	apiUrl: String,
	version: String,
	apiEndpoint: String,
	hcpAuth: String,
	patientCreds: UserCredentials,
	filterTimestamp: Long
) {

	"$version - Can create a new Maintenance Task" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "OTHER",
            status = TaskStatusDto.pending
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
	}

	"$version - Can get a MaintenanceTask as Hcp" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "OTHER",
            status = TaskStatusDto.pending
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		val responseGet = makeGetRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint/${createdTask.id}"
		)
		val retrievedTask = objectMapper.readValue<MaintenanceTaskDto>(responseGet!!)
		retrievedTask.id shouldBe task.id
	}

	"$version - Cannot get a MaintenanceTask as a non delegated Patient" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "OTHER",
            status = TaskStatusDto.pending
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		makeGetRequest(
			createHttpClient(patientCreds.auth()),
			"$apiUrl/$apiEndpoint/${createdTask.id}",
			403
		)
	}

	"$version - Can get a Maintenance Task as a delegated Patient" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "OTHER",
            status = TaskStatusDto.pending,
            delegations = mapOf(patientCreds.dataOwnerId to setOf(DelegationDto()))
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		val responseGet = makeGetRequest(
			createHttpClient(patientCreds.auth()),
			"$apiUrl/$apiEndpoint/${createdTask.id}"
		)
		val retrievedTask = objectMapper.readValue<MaintenanceTaskDto>(responseGet!!)
		retrievedTask.id shouldBe task.id
	}

	"$version - Can delete a MaintenanceTask as Hcp" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "OTHER",
            status = TaskStatusDto.pending
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		val responseDelete = makeDeleteRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint/${createdTask.id}"
		)
		val retrievedTasksId = objectMapper.readValue<List<DocIdentifier>>(responseDelete!!)
		retrievedTasksId.size shouldBe 1
		retrievedTasksId.first().id shouldNotBe null
		retrievedTasksId.first().id shouldBe task.id
	}

	"$version - Cannot delete a MaintenanceTask as a non delegated Patient" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "OTHER",
            status = TaskStatusDto.pending
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		makeDeleteRequest(
			createHttpClient(patientCreds.auth()),
			"$apiUrl/$apiEndpoint/${createdTask.id}",
			403
		)
	}

	"$version - Can delete a Maintenance Task as a delegated Patient" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "OTHER",
            status = TaskStatusDto.pending,
            delegations = mapOf(patientCreds.dataOwnerId to setOf(DelegationDto()))
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		val responseDelete = makeDeleteRequest(
			createHttpClient(patientCreds.auth()),
			"$apiUrl/$apiEndpoint/${createdTask.id}"
		)
		responseDelete shouldNotBe null
		val retrievedTasksId = objectMapper.readValue<List<DocIdentifier>>(responseDelete!!)
		retrievedTasksId.size shouldBe 1
		retrievedTasksId.first().id shouldNotBe null
		retrievedTasksId.first().id shouldBe task.id
	}

	"$version - Can modify a MaintenanceTask as Hcp" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "TYPE-1",
            status = TaskStatusDto.pending
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		createdTask.taskType shouldBe "TYPE-1"
		val responsePut = makePutRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(createdTask.copy(taskType = "TYPE-2"))
		)
		responsePut shouldNotBe null
		val retrievedTask = objectMapper.readValue<MaintenanceTaskDto>(responsePut!!)
		retrievedTask.id shouldBe task.id
		retrievedTask.taskType shouldBe "TYPE-2"
	}

	"$version - Cannot modify a MaintenanceTask as a non delegated Patient" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "TYPE-1",
            status = TaskStatusDto.pending
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		makePutRequest(
			createHttpClient(patientCreds.auth()),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(createdTask.copy(taskType = "TYPE-2")),
			403
		)
	}

	"$version - Can modify a Maintenance Task as a delegated Patient" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "TYPE-1",
            status = TaskStatusDto.pending,
            delegations = mapOf(patientCreds.dataOwnerId to setOf(DelegationDto()))
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		createdTask.taskType shouldBe "TYPE-1"
		val responsePut = makePutRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(createdTask.copy(taskType = "TYPE-2"))
		)
		responsePut shouldNotBe null
		val retrievedTask = objectMapper.readValue<MaintenanceTaskDto>(responsePut!!)
		retrievedTask.id shouldBe task.id
		retrievedTask.taskType shouldBe "TYPE-2"
	}

	"$version - Can filter a MaintenanceTask as Hcp" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "OTHER",
            status = TaskStatusDto.pending,
            delegations = mapOf(ICureTestApplication.masterHcp!!.hcpId to setOf(DelegationDto()))
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		val responseFilter = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint/filter",
			"{\"\$type\":\"FilterChain\",\"filter\":{\"date\":${filterTimestamp},\"\$type\":\"MaintenanceTaskAfterDateFilter\"}}"
		)
		val retrievedTasks = objectMapper.readValue<PaginatedList<MaintenanceTaskDto>>(responseFilter!!)
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

	"$version - Can filter a Maintenance Task as a delegated Patient" {
		val task = MaintenanceTaskDto(
            id = UUID.randomUUID().toString(),
            taskType = "TYPE-1",
            status = TaskStatusDto.pending,
            delegations = mapOf(patientCreds.dataOwnerId to setOf(DelegationDto()))
        )
		val responseBody = makePostRequest(
			createHttpClient(hcpAuth),
			"$apiUrl/$apiEndpoint",
			objectMapper.writeValueAsString(task)
		)
		responseBody shouldNotBe null
		val createdTask = objectMapper.readValue<MaintenanceTaskDto>(responseBody!!)
		createdTask.id shouldBe task.id
		createdTask.taskType shouldBe "TYPE-1"
		val responseFilter = makePostRequest(
			createHttpClient(patientCreds.auth()),
			"$apiUrl/$apiEndpoint/filter",
			"{\"\$type\":\"FilterChain\",\"filter\":{\"ids\":[\"${task.id}\"],\"\$type\":\"MaintenanceTaskByIdsFilter\"}}"
		)
		val retrievedTasks = objectMapper.readValue<PaginatedList<MaintenanceTaskDto>>(responseFilter!!)
		retrievedTasks.rows.size shouldBe 1
		retrievedTasks.rows.first().id shouldBe task.id
	}

}
