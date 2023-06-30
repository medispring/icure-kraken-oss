package org.taktik.icure.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.entities.embed.Content
import org.taktik.icure.test.ICureTestApplication

@SpringBootTest(
	classes = [ICureTestApplication::class],
	properties = [
		"spring.main.allow-bean-definition-overriding=true"
	],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("app")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ObjectMapperTest(
	@Autowired val objectMapper: ObjectMapper
): StringSpec({

	val testFile = "{\"dto\": {\"s\":\"CERTIFICAT DINCAPACIT? DE TRAVAIL SALARI?\"}}"

	"Can parse text with unescaped characters" {
		val tree = objectMapper.readTree(testFile)
		val dto = tree["dto"]
		objectMapper.treeToValue(dto, Content::class.java).let {
			it.stringValue shouldNotBe null
		}

	}


})
