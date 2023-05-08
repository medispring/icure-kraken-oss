package org.taktik.icure.validation

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.mockk.mockk
import org.taktik.icure.test.uuid
import org.taktik.icure.validation.aspect.Fixer

class NotBlankValidatorTest : StringSpec({

	data class FakeData(
		@field:NotBlank(autoFix = AutoFix.UUID) val id: String = "",
		val otherField: String
	)

	val fixer = Fixer<FakeData>(mockk())

	"Auto-fixes a UUID if the field is empty" {
		val data = FakeData(otherField = uuid())
		val fixedData = fixer.fix(data)
		fixedData.id shouldMatch Regex("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\$")
		fixedData.otherField shouldMatch data.otherField
	}

	"Auto-fixes a UUID if the field contains only whitespaces" {
		val data = FakeData(id="   \t\n", otherField = uuid())
		val fixedData = fixer.fix(data)
		fixedData.id shouldMatch Regex("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\$")
		fixedData.otherField shouldMatch data.otherField
	}

	"Does not autofix a UUID if the field contains a non-blank string" {
		val id = "TEST"
		val data = FakeData(id=id, otherField = uuid())
		val fixedData = fixer.fix(data)
		fixedData.id shouldBe id
		fixedData.otherField shouldMatch data.otherField
	}

})
