package org.taktik.icure.asyncdao

import java.io.File
import java.net.URI
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.StringSpec
import kotlin.random.Random.Default.nextInt
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.test.context.ActiveProfiles
import org.taktik.couchdb.ViewQueryResultEvent
import org.taktik.couchdb.ViewRowWithDoc
import org.taktik.icure.asynclogic.CodeLogic
import org.taktik.icure.db.PaginationOffset
import org.taktik.icure.entities.base.Code
import org.taktik.icure.properties.CouchDbProperties
import org.taktik.icure.services.external.rest.v1.dto.CodeDto
import org.taktik.icure.services.external.rest.v1.mapper.base.CodeMapper
import org.taktik.icure.services.external.rest.v1.utils.paginatedList
import org.taktik.icure.test.ICureTestApplication
import org.taktik.icure.test.TestProperties

suspend fun List<Code>.shouldContainAllTheVersions(codeDAO: CodeDAO) =
	this.groupBy {
		Pair(it.type!!, it.code!!)
	}.onEach {
		val versionCounter = codeDAO.listCodeIdsByTypeCodeVersionInterval(
			startType = it.key.first,
			startCode = it.key.second,
			startVersion = null,
			endType = it.key.first,
			endCode = it.key.second,
			endVersion = null
		).count()
		it.value.fold(setOf<String>()){ acc, code -> acc + code.id }.size shouldBe versionCounter
	}

suspend fun List<Code>.shouldContainOnlyLatestVersions(codeDAO: CodeDAO) =
	this.onEach {
		val versionCounter = codeDAO.listCodeIdsByTypeCodeVersionInterval(
			startType = it.type,
			startCode = it.code,
			startVersion = null,
			endType = it.type,
			endCode = it.code,
			endVersion = null
		).toList()
		val latestVersion = versionCounter.maxOrNull()?.let { codeId ->
			codeId.split('|')[2]
		}
		latestVersion shouldNotBe null
		it.version shouldNotBe null
		it.version shouldBe latestVersion
	}

suspend fun Flow<ViewQueryResultEvent>.toListOfCodes() =
	this.filter { it is ViewRowWithDoc<*, *, *> }
		.map { (it as ViewRowWithDoc<*, *, *>).doc as Code }
		.toList()

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
class CodeDAOTest(
	@Autowired val codeDAO: CodeDAO,
	@Autowired val codeMapper: CodeMapper,
	@Autowired val codeLogic: CodeLogic,
	@Autowired val objectMapper: ObjectMapper,
	@Autowired val couchDbProperties: CouchDbProperties,
	@Autowired val testProperties: TestProperties
): StringSpec() {

	val region = "be"
	val language = "fr"
	val codeType = "CD-FED-COUNTRY"
	val startPaginationOffset = PaginationOffset<List<String?>>(null, null, null, 1001)

	init {
		val inputCodes = objectMapper.readValue<List<Code>>(
			File("src/test/resources/org/taktik/icure/db/codes/codes_dao_test.json").inputStream()
		)
		runBlocking {
			val savedCodes = codeDAO.save(inputCodes)
			savedCodes.count() shouldBe inputCodes.size
			val totalUniqueCodes = codeDAO.listCodeIdsByTypeCodeVersionInterval(
				startType = codeType,
				startCode = null,
				startVersion = null,
				endType = codeType,
				endCode = null,
				endVersion = null
			).fold(setOf<String>()) { acc, it ->
				val (type, code, _) = it.split('|')
				acc + "${type}|${code}"
			}.count()

			testLatestVersionFilterOnFindCodesBy(codeDAO, region, codeType, startPaginationOffset, totalUniqueCodes, codeMapper)
			testLatestVersionFilterOnFindCodesByLanguageLabel(codeDAO, region, language, startPaginationOffset, totalUniqueCodes, codeMapper)
			testLatestVersionFilterOnFindCodesByLanguageTypeLabel(codeDAO, region, language, codeType, startPaginationOffset, totalUniqueCodes, codeMapper)
			testLatestVersionFilterOnListCodesBy(codeDAO, region, codeType, totalUniqueCodes)
			testListCodeIdsByTypeCodeVersionInterval(codeDAO, dbInstanceUrl, groupId, inputCodes)
		}
	}
}

private suspend fun StringSpec.testListCodeIdsByTypeCodeVersionInterval(
	codeDAO: CodeDAO,
	dbInstanceUrl: URI,
	groupId: String,
	existingCodes: List<Code>
) {

	val existingIds = existingCodes.map { it.id }.sortedBy { it }
	val sortedCodes = existingCodes.toSortedSet(compareBy { it.id }).toList()

	"All ids are returned if both keys are null" {
		codeDAO.listCodeIdsByTypeCodeVersionInterval(dbInstanceUrl, groupId, null, null, null, null, null, null)
			.onEach {  id ->
				existingIds shouldContain id
			}.count() shouldBe existingCodes.size
	}

	"If the starting key is specified only the results that come after it are returned" {
		val startIndex = nextInt(0, sortedCodes.size)
		val startCode = sortedCodes[startIndex]
		codeDAO.listCodeIdsByTypeCodeVersionInterval(dbInstanceUrl, groupId, startCode.type, startCode.code, startCode.version, null, null, null)
			.onEach {  id ->
				existingIds shouldContain id
				existingIds.indexOf(id) shouldBeGreaterThanOrEqual startIndex
 			}.count() shouldBe (existingCodes.size - startIndex)
	}

	"If the end key is specified only the results that come before it are returned" {
		val endIndex = nextInt(0, sortedCodes.size)
		val endCode = sortedCodes[endIndex]
		codeDAO.listCodeIdsByTypeCodeVersionInterval(dbInstanceUrl, groupId, null, null, null, endCode.type, endCode.code, endCode.version)
			.onEach {  id ->
				existingIds shouldContain id
				existingIds.indexOf(id) shouldBeLessThanOrEqual endIndex
			}.count() shouldBe (endIndex + 1)
	}

	"If the start key and the end key are specified all the in-between results are returned" {
		val startIndex = nextInt(0, sortedCodes.size/2)
		val startCode = sortedCodes[startIndex]
		val endIndex = nextInt(sortedCodes.size/2, sortedCodes.size)
		val endCode = sortedCodes[endIndex]
		codeDAO.listCodeIdsByTypeCodeVersionInterval(dbInstanceUrl, groupId, startCode.type, startCode.code, startCode.version, endCode.type, endCode.code, endCode.version)
			.onEach {  id ->
				existingIds shouldContain id
				existingIds.indexOf(id) shouldBeGreaterThanOrEqual startIndex
				existingIds.indexOf(id) shouldBeLessThanOrEqual endIndex
			}.count() shouldBe (endIndex + 1 - startIndex)
	}

}

private suspend fun StringSpec.testLatestVersionFilterOnListCodesBy(
	codeDAO: CodeDAO,
	region: String,
	codeType: String,
	totalUniqueCodes: Int
){

	"listCodesBy should return all the versions of the codes if no version or code are specified" {
		val codes = codeDAO.listCodesBy(region, codeType, null, null).toList()
		codes.size shouldBeGreaterThan 0
		codes.shouldContainAllTheVersions(codeDAO)
	}

	"listCodesBy should return all the versions of a single code if no version is specified " {
		val codes = codeDAO.listCodesBy(region, codeType, "pg", null).toList()
		codes.size shouldBeGreaterThan 0
		codes.shouldContainAllTheVersions(codeDAO)
	}

	"listCodesBy should return a single version of a single code if all parameters are specified" {
		val version = "1.2"
		val codes = codeDAO.listCodesBy(region, codeType, "pg", version).toList()
		codes.size shouldBe 1
		codes[0].version shouldNotBe null
		codes[0].version shouldBe version
	}

	"listCodesBy should return the latest version of a single code if all parameters are specified" {
		val codes = codeDAO.listCodesBy(region, codeType, "pg", "latest").toList()
		codes.size shouldBe 1
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

	"listCodesBy should return the latest version of all the codes if no code is specified" {
		val codes = codeDAO.listCodesBy(region, codeType, null, "latest").toList()
		codes.size shouldBe totalUniqueCodes
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

}

private suspend fun StringSpec.testLatestVersionFilterOnFindCodesByLanguageTypeLabel(
	codeDAO: CodeDAO,
	region: String,
	language: String,
	codeType: String,
	startPaginationOffset: PaginationOffset<List<String?>>,
	totalUniqueCodes: Int,
	codeMapper: CodeMapper,
	codeToCodeDto: (Code) -> CodeDto = { it: Code -> codeMapper.map(it) }
) {

	"findCodesBy should return all the versions of the codes if no version, type, or label are specified" {
		val codes = codeDAO.findCodesByLabel(region, language, null, null, null, startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThanOrEqual totalUniqueCodes
		codes.shouldContainAllTheVersions(codeDAO)
	}

	"findCodesBy should return all the versions of the matching codes if no version or label are specified" {
		val codes = codeDAO.findCodesByLabel(region, language, codeType, null, null, startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThan 0
		codes.shouldContainAllTheVersions(codeDAO)
	}

	"findCodesBy should return all the versions of the matching codes of a type if no version is specified" {
		val codes = codeDAO.findCodesByLabel(region, language, codeType, "al", null, startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThan 0
		codes.shouldContainAllTheVersions(codeDAO)
	}

	"findCodesBy should return a specific version of all the codes if a version and type but no label are specified" {
		val version = "1.2"
		val codes = codeDAO.findCodesByLabel(region, language, codeType, null, version, startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThanOrEqual totalUniqueCodes
		codes.onEach {
			it.version shouldNotBe null
			it.version shouldBe version
		}
	}

	"findCodesBy should return a specific version of the codes if a version and type but no label are specified paginating the results" {
		val version = "1.2"
		val totalSize = codeDAO.findCodesByLabel(region, language, codeType,null, version, startPaginationOffset)
			.toListOfCodes().size
		totalSize shouldBeGreaterThanOrEqual totalUniqueCodes
		val pageSize = totalSize / 2
		val reducedPaginationOffset = PaginationOffset<List<String?>>(null, null, null, pageSize + 1)
		var page = codeDAO.findCodesByLabel(region, language, codeType, null, version, reducedPaginationOffset)
			.paginatedList(codeToCodeDto, pageSize)
		val codes = page.rows.map { codeMapper.map(it) }.toMutableList()
		while(page.nextKeyPair != null) {
			val newOffset = PaginationOffset<List<String?>>(
				(page.nextKeyPair!!.startKey!! as Array<String>).toList(),
				page.nextKeyPair!!.startKeyDocId, null, pageSize + 1)
			page = codeDAO.findCodesByLabel(region, language, codeType, null, version, newOffset)
				.paginatedList(codeToCodeDto, pageSize)
			codes.addAll(page.rows.map { codeMapper.map(it) })
		}
		codes.size shouldBe totalSize
		codes.onEach {
			it.version shouldNotBe null
			it.version shouldBe version
		}
	}

	"findCodesBy should return a specific version of all the codes if latest version and type but no label are specified" {
		val codes = codeDAO.findCodesByLabel(region, language, codeType, null, "latest", startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThanOrEqual totalUniqueCodes
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

	"findCodesBy should return a specific version of the codes if latest version and type but no label are specified paginating the results dividing them in a half" {
		val totalSize = codeDAO.findCodesByLabel(region, language, codeType, null, "latest", startPaginationOffset)
			.toListOfCodes().size
		totalSize shouldBeGreaterThanOrEqual totalUniqueCodes
		val pageSize = totalSize / 2
		val reducedPaginationOffset = PaginationOffset<List<String?>>(null, null, null, pageSize + 1)
		var page = codeDAO.findCodesByLabel(region, language, codeType, null, "latest", reducedPaginationOffset)
			.paginatedList(codeToCodeDto, pageSize)
		val codes = page.rows.map { codeMapper.map(it) }.toMutableList()
		while(page.nextKeyPair != null) {
			val newOffset = PaginationOffset<List<String?>>(
				(page.nextKeyPair!!.startKey!! as Array<String>).toList(),
				page.nextKeyPair!!.startKeyDocId, null, pageSize + 1)
			page = codeDAO.findCodesByLabel(region, language, codeType, null, "latest", newOffset)
				.paginatedList(codeToCodeDto, pageSize)
			codes.addAll(page.rows.map { codeMapper.map(it) })
		}
		codes.size shouldBeGreaterThanOrEqual totalSize
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

	"findCodesBy should return a specific version of all the codes if a version, type, and a label are specified" {
		val version = "1.2"
		val codes = codeDAO.findCodesByLabel(region, language, codeType, "al", version, startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThan 0
		codes.onEach {
			it.version shouldNotBe null
			it.version shouldBe version
		}
	}

	"findCodesBy should return a specific version of the codes if a version, type, and label are specified paginating the results" {
		val version = "1.2"
		val label = "al"
		val totalSize = codeDAO.findCodesByLabel(region, language, codeType, label, version, startPaginationOffset)
			.toListOfCodes().size
		totalSize shouldBeGreaterThan 0
		val pageSize = totalSize / 2
		val reducedPaginationOffset = PaginationOffset<List<String?>>(null, null, null, pageSize + 1)
		var page = codeDAO.findCodesByLabel(region, language, codeType, label, version, reducedPaginationOffset)
			.paginatedList(codeToCodeDto, pageSize)
		val codes = page.rows.map { codeMapper.map(it) }.toMutableList()
		while(page.nextKeyPair != null) {
			val newOffset = PaginationOffset<List<String?>>(
				(page.nextKeyPair!!.startKey!! as Array<String>).toList(),
				page.nextKeyPair!!.startKeyDocId, null, pageSize + 1)
			page = codeDAO.findCodesByLabel(region, language, codeType, label, version, newOffset)
				.paginatedList(codeToCodeDto, pageSize)
			codes.addAll(page.rows.map { codeMapper.map(it) })
		}
		codes.size shouldBe totalSize
		codes.onEach {
			it.version shouldNotBe null
			it.version shouldBe version
		}
	}

	"findCodesBy should return a specific version of all the codes if latest version, type, and a label are specified" {
		val codes = codeDAO.findCodesByLabel(region, language, codeType, "al", "latest", startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThan 0
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

	"findCodesBy should return a specific version of the codes if latest version, type, and a label are specified paginating the results dividing them in a half" {
		val label = "al"
		val totalSize = codeDAO.findCodesByLabel(region, language, codeType, label, "latest", startPaginationOffset)
			.toListOfCodes().size
		totalSize shouldBeGreaterThan 0
		val pageSize = totalSize / 2
		val reducedPaginationOffset = PaginationOffset<List<String?>>(null, null, null, pageSize + 1)
		var page = codeDAO.findCodesByLabel(region, language, codeType, label, "latest", reducedPaginationOffset)
			.paginatedList(codeToCodeDto, pageSize)
		val codes = page.rows.map { codeMapper.map(it) }.toMutableList()
		while(page.nextKeyPair != null) {
			val newOffset = PaginationOffset<List<String?>>(
				(page.nextKeyPair!!.startKey!! as Array<String>).toList(),
				page.nextKeyPair!!.startKeyDocId, null, pageSize + 1)
			page = codeDAO.findCodesByLabel(region, language, codeType, label, "latest", newOffset)
				.paginatedList(codeToCodeDto, pageSize)
			codes.addAll(page.rows.map { codeMapper.map(it) })
		}
		codes.size shouldBeGreaterThanOrEqual totalSize
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}
}

private suspend fun StringSpec.testLatestVersionFilterOnFindCodesByLanguageLabel(
	codeDAO: CodeDAO,
	region: String,
	language: String,
	startPaginationOffset: PaginationOffset<List<String?>>,
	totalUniqueCodes: Int,
	codeMapper: CodeMapper,
	codeToCodeDto: (Code) -> CodeDto = { it: Code -> codeMapper.map(it) }
) {

	"findCodesBy should return all the versions of the codes if no version or label are specified" {
		val codes = codeDAO.findCodesByLabel(region, language, null, null, startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThanOrEqual totalUniqueCodes
		codes.shouldContainAllTheVersions(codeDAO)
	}

	"findCodesBy should return all the versions of the matching codes if no version is specified" {
		val codes = codeDAO.findCodesByLabel(region, language, "al", null, startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThan 0
		codes.shouldContainAllTheVersions(codeDAO)
	}

	"findCodesBy should return a specific version of all the codes if a version but no label are specified" {
		val version = "1.2"
		val codes = codeDAO.findCodesByLabel(region, language, null, version, startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThanOrEqual totalUniqueCodes
		codes.onEach {
			it.version shouldNotBe null
			it.version shouldBe version
		}
	}

	"findCodesBy should return a specific version of the codes if a version but no label are specified paginating the results" {
		val version = "1.2"
		val totalSize = codeDAO.findCodesByLabel(region, language, null, version, startPaginationOffset)
			.toListOfCodes().size
		totalSize shouldBeGreaterThanOrEqual totalUniqueCodes
		val pageSize = totalSize / 2
		val reducedPaginationOffset = PaginationOffset<List<String?>>(null, null, null, pageSize + 1)
		var page = codeDAO.findCodesByLabel(region, language, null, version, reducedPaginationOffset)
			.paginatedList(codeToCodeDto, pageSize)
		val codes = page.rows.map { codeMapper.map(it) }.toMutableList()
		while(page.nextKeyPair != null) {
			val newOffset = PaginationOffset<List<String?>>(
				(page.nextKeyPair!!.startKey!! as Array<String>).toList(),
				page.nextKeyPair!!.startKeyDocId, null, pageSize + 1)
			page = codeDAO.findCodesByLabel(region, language, null, version, newOffset)
				.paginatedList(codeToCodeDto, pageSize)
			codes.addAll(page.rows.map { codeMapper.map(it) })
		}
		codes.size shouldBe totalSize
		codes.onEach {
			it.version shouldNotBe null
			it.version shouldBe version
		}
	}

	"findCodesBy should return a specific version of all the codes if latest version but no label are specified" {
		val codes = codeDAO.findCodesByLabel(region, language, null, "latest", startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThanOrEqual totalUniqueCodes
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

	"findCodesBy should return a specific version of the codes if latest version but no label are specified paginating the results dividing them in a half" {
		val totalSize = codeDAO.findCodesByLabel(region, language, null, "latest", startPaginationOffset)
			.toListOfCodes().size
		totalSize shouldBeGreaterThanOrEqual totalUniqueCodes
		val pageSize = totalSize / 2
		val reducedPaginationOffset = PaginationOffset<List<String?>>(null, null, null, pageSize + 1)
		var page = codeDAO.findCodesByLabel(region, language, null, "latest", reducedPaginationOffset)
			.paginatedList(codeToCodeDto, pageSize)
		val codes = page.rows.map { codeMapper.map(it) }.toMutableList()
		while(page.nextKeyPair != null) {
			val newOffset = PaginationOffset<List<String?>>(
				(page.nextKeyPair!!.startKey!! as Array<String>).toList(),
				page.nextKeyPair!!.startKeyDocId, null, pageSize + 1)
			page = codeDAO.findCodesByLabel(region, language, null, "latest", newOffset)
				.paginatedList(codeToCodeDto, pageSize)
			codes.addAll(page.rows.map { codeMapper.map(it) })
		}
		codes.size shouldBeGreaterThanOrEqual totalSize
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

	"findCodesBy should return a specific version of all the codes if a version and a label are specified" {
		val version = "1.2"
		val codes = codeDAO.findCodesByLabel(region, language, "al", version, startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThan 0
		codes.onEach {
			it.version shouldNotBe null
			it.version shouldBe version
		}
	}

	"findCodesBy should return a specific version of the codes if a version and a label are specified paginating the results" {
		val version = "1.2"
		val label = "al"
		val totalSize = codeDAO.findCodesByLabel(region, language, label, version, startPaginationOffset)
			.toListOfCodes().size
		totalSize shouldBeGreaterThan 0
		val pageSize = totalSize / 2
		val reducedPaginationOffset = PaginationOffset<List<String?>>(null, null, null, pageSize + 1)
		var page = codeDAO.findCodesByLabel(region, language, label, version, reducedPaginationOffset)
			.paginatedList(codeToCodeDto, pageSize)
		val codes = page.rows.map { codeMapper.map(it) }.toMutableList()
		while(page.nextKeyPair != null) {
			val newOffset = PaginationOffset<List<String?>>(
				(page.nextKeyPair!!.startKey!! as Array<String>).toList(),
				page.nextKeyPair!!.startKeyDocId, null, pageSize + 1)
			page = codeDAO.findCodesByLabel(region, language, label, version, newOffset)
				.paginatedList(codeToCodeDto, pageSize)
			codes.addAll(page.rows.map { codeMapper.map(it) })
		}
		codes.size shouldBe totalSize
		codes.onEach {
			it.version shouldNotBe null
			it.version shouldBe version
		}
	}

	"findCodesBy should return a specific version of all the codes if latest version and a label are specified" {
		val codes = codeDAO.findCodesByLabel(region, language, "al", "latest", startPaginationOffset)
			.toListOfCodes()
		codes.size shouldBeGreaterThan 0
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

	"findCodesBy should return a specific version of the codes if latest version and a label are specified paginating the results dividing them in a half" {
		val label = "al"
		val totalSize = codeDAO.findCodesByLabel(region, language, label, "latest", startPaginationOffset)
			.toListOfCodes().size
		totalSize shouldBeGreaterThan 0
		val pageSize = totalSize / 2
		val reducedPaginationOffset = PaginationOffset<List<String?>>(null, null, null, pageSize + 1)
		var page = codeDAO.findCodesByLabel(region, language, label, "latest", reducedPaginationOffset)
			.paginatedList(codeToCodeDto, pageSize)
		val codes = page.rows.map { codeMapper.map(it) }.toMutableList()
		while(page.nextKeyPair != null) {
			val newOffset = PaginationOffset<List<String?>>(
				(page.nextKeyPair!!.startKey!! as Array<String>).toList(),
				page.nextKeyPair!!.startKeyDocId, null, pageSize + 1)
			page = codeDAO.findCodesByLabel(region, language, label, "latest", newOffset)
				.paginatedList(codeToCodeDto, pageSize)
			codes.addAll(page.rows.map { codeMapper.map(it) })
		}
		codes.size shouldBeGreaterThanOrEqual totalSize
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}
}

private suspend fun StringSpec.testLatestVersionFilterOnFindCodesBy(
	codeDAO: CodeDAO,
	region: String,
	codeType: String,
	startPaginationOffset: PaginationOffset<List<String?>>,
	totalUniqueCodes: Int,
	codeMapper: CodeMapper,
	codeToCodeDto: (Code) -> CodeDto = { it: Code -> codeMapper.map(it) }
){

	"findCodesBy should return all the versions of the codes if no version or code are specified" {
		val codes = codeDAO.findCodesBy(region, codeType, null, null, startPaginationOffset, 1f)
			.toListOfCodes()
		codes.size shouldBeGreaterThan 0
		codes.shouldContainAllTheVersions(codeDAO)
	}

	"findCodesBy should return all the versions of a single code if no version is specified " {
		val codes = codeDAO.findCodesBy(region, codeType, "pg", null, startPaginationOffset, 1f)
			.toListOfCodes()
		codes.size shouldBeGreaterThan 0
		codes.shouldContainAllTheVersions(codeDAO)
	}

	"findCodesBy should return a single version of a single code if all parameters are specified" {
		val version = "1.2"
		val codes = codeDAO.findCodesBy(region, codeType, "pg", version, startPaginationOffset, 1f)
			.toListOfCodes()
		codes.size shouldBe 1
		codes[0].version shouldNotBe null
		codes[0].version shouldBe version
	}

	"findCodesBy should return the latest version of a single code if all parameters are specified" {
		val codes = codeDAO.findCodesBy(region, codeType, "pg", "latest", startPaginationOffset, 1f)
			.toListOfCodes()
		codes.size shouldBe 1
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

	"findCodesBy should return the latest version of all the codes if no code is specified" {
		val codes = codeDAO.findCodesBy(region, codeType, null, "latest", startPaginationOffset, 1f)
			.toListOfCodes()
		codes.size shouldBe totalUniqueCodes
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

	"findCodesBy should return the latest version of all the codes if no code is specified and the results are paginated" {
		val pageSize = totalUniqueCodes / 2
		val reducedPaginationOffset = PaginationOffset<List<String?>>(null, null, null, pageSize + 1)
		var page = codeDAO.findCodesBy(region, codeType, null, "latest", reducedPaginationOffset, 1f)
			.paginatedList(codeToCodeDto, pageSize)
		val codes = page.rows.map { codeMapper.map(it) }.toMutableList()
		while(page.nextKeyPair != null) {
			val newOffset = PaginationOffset<List<String?>>(
				(page.nextKeyPair!!.startKey!! as Array<String>).toList(),
				page.nextKeyPair!!.startKeyDocId, null, pageSize + 1)
			page = codeDAO.findCodesBy(region, codeType, null, "latest", newOffset, 1f)
				.paginatedList(codeToCodeDto, pageSize)
			codes.addAll(page.rows.map { codeMapper.map(it) })
		}
		codes.size shouldBe totalUniqueCodes
		codes.shouldContainOnlyLatestVersions(codeDAO)
	}

}
