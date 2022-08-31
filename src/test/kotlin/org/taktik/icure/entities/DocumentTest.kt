package org.taktik.icure.entities

import java.util.UUID
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.taktik.icure.entities.embed.DocumentType

class DocumentTest : StringSpec({

	"Retrieving mainAttachment when having mainUti in otherUtis is successful" {
		// Given
		val document = Document(
			id = UUID.randomUUID().toString(),
			name = "Testing Document",
			rev = "887655645856877809889",
			documentType = DocumentType.report,
			mainUti = "org.icure.test.report",
			otherUtis = setOf("org.icure.test.report", "org.icure.test.other.report"),
			attachmentId = UUID.randomUUID().toString()
		)

		// When
		val dataAttachment = document.mainAttachment

		// Then
		dataAttachment.shouldNotBeNull()
		dataAttachment.couchDbAttachmentId shouldBe document.attachmentId
		dataAttachment.objectStoreAttachmentId.shouldBeNull()
	}
})
