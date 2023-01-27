package org.taktik.icure.test.generators

import kotlin.random.Random
import org.taktik.icure.services.external.rest.v1.dto.CodeDto

class CodeBatchGenerator {

	private val alphabet: List<Char> = ('a'..'z').toList() + ('A'..'Z') + ('0'..'9')
	private val languages = listOf("en", "fr", "nl")
	private val types = listOf("SNOMED", "LOINC", "TESTCODE", "DEEPSECRET")
	private val regions = listOf("int", "fr", "be")

	private fun generateRandomString(length: Int) = (1..length)
		.map { _ -> alphabet[Random.nextInt(0, alphabet.size)] }
		.joinToString("")

	fun createBatchOfUniqueCodes(size: Int) = (1..size)
		.fold(listOf<CodeDto>()) { acc, _ ->
			val lang = languages[Random.nextInt(0, languages.size)]
			val type = types[Random.nextInt(0, types.size)]
			val code = generateRandomString(20)
			val version = Random.nextInt(0, 10).toString()
			acc + CodeDto(
                id = "$type|$code|$version",
                type = type,
                code = code,
                version = version,
                label = if (Random.nextInt(0, 4) == 0) mapOf(
                    lang to generateRandomString(
                        Random.nextInt(
                            20,
                            100
                        )
                    )
                ) else mapOf(),
                regions = if (Random.nextInt(0, 4) == 0) setOf(regions[Random.nextInt(0, regions.size)]) else setOf(),
                qualifiedLinks = if (Random.nextInt(0, 4) == 0) mapOf(
                    generateRandomString(10) to List(
                        Random.nextInt(
                            1,
                            4
                        )
                    ) { generateRandomString(10) }) else mapOf(),
                searchTerms = if (Random.nextInt(0, 4) == 0) mapOf(
                    generateRandomString(10) to List(
                        Random.nextInt(
                            1,
                            4
                        )
                    ) { generateRandomString(10) }.toSet()
                ) else mapOf(),
            )
		}

	fun randomCodeModification(code: CodeDto, modifyLabel: Boolean = false, modifyRegions: Boolean = false, modifyQualifiedLinks: Boolean = false, modifySearchTerms: Boolean = false) = code
		.let {
			if (modifyLabel && Random.nextInt(0, 4) == 0) it.copy(label = mapOf(languages[Random.nextInt(
                0,
                languages.size
            )] to generateRandomString(Random.nextInt(20, 100))))
			else it
		}.let {
			if (modifyRegions && Random.nextInt(0, 4) == 0) it.copy(regions = setOf(regions[Random.nextInt(
                0,
                regions.size
            )]))
			else it
		}.let {
			if (modifyQualifiedLinks && Random.nextInt(0, 4) == 0) it.copy(qualifiedLinks = mapOf(generateRandomString(10) to List(
                Random.nextInt(1, 4)
            ) { generateRandomString(10) }))
			else it
		}.let {
			if (modifySearchTerms && Random.nextInt(0, 4) == 0) it.copy(searchTerms = mapOf(generateRandomString(10) to List(
                Random.nextInt(1, 4)
            ) { generateRandomString(10) }.toSet()))
			else it
		}
}
