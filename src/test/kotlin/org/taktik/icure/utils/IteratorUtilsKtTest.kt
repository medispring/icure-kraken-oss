package org.taktik.icure.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith

class IteratorUtilsKtTest : FunSpec({

	test("sortedMerge basic") {
		listOf(
			listOf(1, 3, 7, 9).iterator(),
			listOf(2, 4, 6, 8).iterator(),
			listOf(5, 8, 11, 12).iterator()
		).sortedMerge().asSequence().toList() shouldBe listOf(1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 11, 12)
	}

	test("Edge case: one iterator has only one element that is the last one to be emitted") {
		listOf(
			listOf(1, 3, 5, 7).iterator(),
			listOf(42).iterator()
		).sortedMerge().asSequence().toList() shouldBe listOf(1, 3, 5, 7, 42)
	}

	test("Edge case: one iterator has only one element") {
		listOf(
			listOf(1, 3, 5, 42).iterator(),
			listOf(7).iterator()
		).sortedMerge().asSequence().toList() shouldBe listOf(1, 3, 5, 7, 42)
	}

	test("illegal inputs are discarded") {
		val exception = shouldThrow<IllegalStateException> {
			listOf(
				listOf(3, 1, 7, 9).iterator(),
				listOf(2, 4, 6, 8).iterator(),
				listOf(5, 8, 11, 12).iterator()
			).sortedMerge().asSequence().toList()
		}
		exception.message shouldStartWith "Provided iterators must be sorted"
	}

})
