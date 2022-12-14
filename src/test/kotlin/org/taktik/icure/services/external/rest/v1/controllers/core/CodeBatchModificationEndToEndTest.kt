package org.taktik.icure.services.external.rest.v1.controllers.core

import java.net.URI
import com.fasterxml.jackson.core.type.TypeReference
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.asyncdao.CodeDAO
import org.taktik.icure.properties.CouchDbProperties
import org.taktik.icure.services.external.rest.v1.dto.CodeDto
import org.taktik.icure.services.external.rest.v1.mapper.base.CodeMapper
import org.taktik.icure.test.ICureTestApplication
import org.taktik.icure.test.generators.CodeBatchGenerator
import org.taktik.icure.test.makePutRequest
import org.taktik.icure.test.objectMapper

@SpringBootTest(
	classes = [ICureTestApplication::class],
	properties = ["spring.main.allow-bean-definition-overriding=true"],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("app")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CodeBatchModificationEndToEndTest @Autowired constructor(
	private val codeDAO: CodeDAO,
	private val codeMapper: CodeMapper,
	private val couchDbProperties: CouchDbProperties,
	@LocalServerPort val port: Int,
) {
	val apiUrl = System.getenv("ICURE_URL") ?: "http://localhost"
	val apiVersion: String = System.getenv("API_VERSION") ?: "v1"
	private final val codeGenerator = CodeBatchGenerator()
	private final val batchSize = 1001
	var existingCodes: List<CodeDto> = listOf()

	init {
		runBlocking {
			val codes = codeGenerator.createBatchOfUniqueCodes(batchSize)
			existingCodes = codeDAO.create(codes.map { codeMapper.map(it) })
				.map { codeMapper.map(it) }
				.toList()
		}
	}

	private fun apiUrl() = if (apiUrl == "http://localhost") "$apiUrl:$port" else apiUrl
	private fun codeApiEndpoint() = "${apiUrl()}/rest/$apiVersion/code"

	fun createHttpClient() = org.taktik.icure.test.createHttpClient("john", "LetMeIn")

	private suspend fun getCode(id: String) = codeDAO.get(id)

	fun verifyExistingCodes(codes: List<CodeDto>) {
		runBlocking {
			codes.forEach {
				assertEquals(codeMapper.map(it), getCode(it.id))
			}
		}
	}

	fun verifyCorrectBatch(requestCodes: List<CodeDto>, responseCodes: List<CodeDto>) {
		// Check that the provided response is correct
		assertEquals(requestCodes.size, responseCodes.size)

		responseCodes.zip(requestCodes).forEach {
			assertNotEquals(it.second.rev, it.first.rev)
			assertEquals(it.second.copy(rev = ""), it.first.copy(rev = ""))
		}

		// Check that all the new codes are in the database
		verifyExistingCodes(responseCodes)
	}

	@Test
	fun batchModificationCanChangeLabel() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true) }

		val responseString = makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(modifiedCodes))
		assertNotNull(responseString)
		val response = objectMapper.readValue(responseString!!, object : TypeReference<List<CodeDto>>() {})

		verifyCorrectBatch(modifiedCodes, response)
		existingCodes = response
	}

	@Test
	fun batchModificationCanChangeRegion() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = false, modifyRegions = true) }

		val responseString = makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(modifiedCodes))
		assertNotNull(responseString)
		val response = objectMapper.readValue(responseString!!, object : TypeReference<List<CodeDto>>() {})

		verifyCorrectBatch(modifiedCodes, response)
		existingCodes = response
	}

	@Test
	fun batchModificationCanChangeQualifiedLinks() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = false, modifyRegions = false, modifyQualifiedLinks = true) }

		val responseString = makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(modifiedCodes))
		assertNotNull(responseString)
		val response = objectMapper.readValue(responseString!!, object : TypeReference<List<CodeDto>>() {})

		verifyCorrectBatch(modifiedCodes, response)
		existingCodes = response
	}

	@Test
	fun batchModificationCanChangeSearchTerms() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = false, modifyRegions = false, modifyQualifiedLinks = false, modifySearchTerms = true) }

		val responseString = makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(modifiedCodes))
		assertNotNull(responseString)
		val response = objectMapper.readValue(responseString!!, object : TypeReference<List<CodeDto>>() {})

		verifyCorrectBatch(modifiedCodes, response)
		existingCodes = response
	}

	@Test
	fun batchModificationCanChangeMultipleParameters() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }

		val responseString = makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(modifiedCodes))
		assertNotNull(responseString)
		val response = objectMapper.readValue(responseString!!, object : TypeReference<List<CodeDto>>() {})

		verifyCorrectBatch(modifiedCodes, response)
		existingCodes = response
	}

	@Test
	fun batchModificationFailsIfAtLeastOneCodeDoesNotExist() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(id = "DUMMYID")

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(incorrectBatch), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationFailsIfAtLeastOneCodeHasNullType() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(type = null)

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(incorrectBatch), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationFailsIfAtLeastOneCodeModifiesType() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(type = "DUMMYTYPE")

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(incorrectBatch), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationFailsIfAtLeastOneCodeHasNullCode() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(code = null)

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(incorrectBatch), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationFailsIfAtLeastOneCodeModifiesCode() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(type = "DUMMYCODE")

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(incorrectBatch), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationFailsIfAtLeastOneCodeHasNullVersion() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(version = null)

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(incorrectBatch), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationFailsIfAtLeastOneCodeModifiesVersion() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(version = "DUMMYVERSION")

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(incorrectBatch), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationFailsIfLabelIsNotAMap() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(label = mapOf("DUMMY_LANG" to "DUMMY_VAL"))
		val incorrectStringBatch = objectMapper.writeValueAsString(incorrectBatch)

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", incorrectStringBatch.replace("\\{ *\"DUMMY_LANG\" *: *\"DUMMY_VAL\" *}".toRegex(), "\"DUMMY\""), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationFailsIfRegionsIsNotASet() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(regions = setOf("DUMMY_REGION"))
		val incorrectStringBatch = objectMapper.writeValueAsString(incorrectBatch)

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", incorrectStringBatch.replace("\\[ *\"DUMMY_REGION\" *]".toRegex(), "\"DUMMY\""), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationFailsIfQualifiedLinksIsNotAMap() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(qualifiedLinks = mapOf("DUMMY_TYPE" to listOf("DUMMY_CODE")))
		val incorrectStringBatch = objectMapper.writeValueAsString(incorrectBatch)

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", incorrectStringBatch.replace("\\{ *\"DUMMY_TYPE\" *: *\\[ *\"DUMMY_CODE\" *] *}".toRegex(), "\"DUMMY\""), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationFailsIfSearchTermsIsNotAMap() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }
		val incorrectBatch = modifiedCodes.subList(1, modifiedCodes.size) + modifiedCodes[0].copy(searchTerms = mapOf("DUMMY_LANG" to setOf("DUMMY_TERM")))
		val incorrectStringBatch = objectMapper.writeValueAsString(incorrectBatch)

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", incorrectStringBatch.replace("\\{ *\"DUMMY_LANG\" *: *\\[ *\"DUMMY_TERM\" *] *}".toRegex(), "\"DUMMY\""), 400)

		verifyExistingCodes(existingCodes)
	}

	@Test
	fun emptyBatchLeadsToNoModification() {
		val responseString = makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(listOf<CodeDto>()))
		assertNotNull(responseString)
		val response = objectMapper.readValue(responseString!!, object : TypeReference<List<CodeDto>>() {})
		assertEquals(0, response.size)
		verifyExistingCodes(existingCodes)
	}

	@Test
	fun batchModificationWithDuplicateCodeFails() {
		val modifiedCodes = existingCodes.map { codeGenerator.randomCodeModification(it, modifyLabel = true, modifyRegions = true, modifyQualifiedLinks = true, modifySearchTerms = true) }

		makePutRequest(createHttpClient(), "${codeApiEndpoint()}/batch", objectMapper.writeValueAsString(modifiedCodes + modifiedCodes[0]), 400)

		verifyExistingCodes(existingCodes)
	}
}
