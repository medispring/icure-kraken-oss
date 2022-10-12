package org.taktik.icure.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.ints.shouldBeGreaterThan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.runBlocking
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.taktik.icure.test.asDataBufferFlow
import org.taktik.icure.test.randomBytes
import org.taktik.icure.test.shouldContainExactly

class FlowUtilsTest : StringSpec({
	val allBytes = (Byte.MIN_VALUE..Byte.MAX_VALUE).map { it.toByte() }

	fun allBytesDataBufferFlow(bufferSize: Int): Flow<DataBuffer> =
		allBytes.chunked(bufferSize).map {
			DefaultDataBufferFactory.sharedInstance.wrap(it.toByteArray())
		}.asFlow()

	suspend fun Flow<DataBuffer>.testDropping(drop: Int) {
		dropBytes(drop.toLong()).toByteArray(true).toList() shouldContainExactly allBytes.drop(drop)
	}

	"Drop bytes should work if n corresponds to an intermediate position of a buffer" {
		allBytesDataBufferFlow(20).testDropping(5)
		allBytesDataBufferFlow(12).testDropping(15)
		allBytesDataBufferFlow(17).testDropping(100)
	}

	"Drop bytes should work if n requires to begin at the first byte of an intermediate buffer" {
		allBytesDataBufferFlow(40).testDropping(40)
		allBytesDataBufferFlow(15).testDropping(30)
		allBytesDataBufferFlow(5).testDropping(155)
	}

	"Drop bytes should work if n requires to begin at the last byte of a buffer" {
		allBytesDataBufferFlow(40).testDropping(39)
		allBytesDataBufferFlow(17).testDropping(33)
		allBytesDataBufferFlow(50).testDropping(149)
	}

	"Drop bytes should work if n requires to begin at the second byte of a buffer" {
		allBytesDataBufferFlow(30).testDropping(31)
		allBytesDataBufferFlow(60).testDropping(121)
		allBytesDataBufferFlow(25).testDropping(176)
	}

	"Drop bytes should work if n requires to drop all bytes except for the last" {
		allBytesDataBufferFlow(99).testDropping(allBytes.size - 1)
		allBytesDataBufferFlow(2).testDropping(allBytes.size - 1)
		allBytesDataBufferFlow(130).testDropping(allBytes.size - 1)
	}

	"Drop bytes should work if n requires to drop all bytes" {
		allBytesDataBufferFlow(24).testDropping(allBytes.size)
		allBytesDataBufferFlow(90).testDropping(allBytes.size)
		allBytesDataBufferFlow(82).testDropping(allBytes.size)
	}

	"Buffer first size with empty flow should give empty flow" {
		flowOf<DataBuffer>().bufferFirstSize(10).toList().shouldBeEmpty()
	}

	suspend fun Flow<DataBuffer>.toByteArrayList() = toList().map { it.toByteArray(true) }

	fun List<ByteArray>.merge() = flatMap { it.toList() }.toByteArray()

	suspend fun testBufferFirst(size: Int, vararg bytesArrays: ByteArray) {
		val bytes = bytesArrays.toList()
		val merged = bytes.merge()
		val buffered = bytes.asDataBufferFlow().bufferFirstSize(size).toByteArrayList()
		if (merged.size >= size) {
			buffered.first().size shouldBeGreaterThanOrEqualTo size
			buffered.merge() shouldContainExactly merged
		} else {
			buffered.shouldHaveSize(1)
			buffered.first() shouldContainExactly merged
		}
	}

	"Buffer first size should merge all data buffers into a single one if the limit is not reached" {
		testBufferFirst(100, randomBytes(20), randomBytes(30), randomBytes(39), randomBytes(10))
		testBufferFirst(100, randomBytes(99))
		testBufferFirst(100, randomBytes(5), randomBytes(10))
		testBufferFirst(100, randomBytes(5))
	}

	"Buffer first size should merge multiple data buffers into a single until the limit reached" {
		testBufferFirst(100, randomBytes(100), randomBytes(20), randomBytes(39), randomBytes(10))
		testBufferFirst(100, randomBytes(110), randomBytes(2), randomBytes(3))
		testBufferFirst(100, randomBytes(5), randomBytes(10), randomBytes(90), randomBytes(2))
		testBufferFirst(100, randomBytes(5), randomBytes(100), randomBytes(20), randomBytes(19))
		testBufferFirst(100, randomBytes(100))
	}
})
