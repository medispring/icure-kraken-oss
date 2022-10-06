package org.taktik.icure.asynclogic.samv2

import java.util.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.taktik.icure.asyncdao.samv2.AmpDAO
import org.taktik.icure.asyncdao.samv2.NmpDAO
import org.taktik.icure.asyncdao.samv2.ParagraphDAO
import org.taktik.icure.asyncdao.samv2.PharmaceuticalFormDAO
import org.taktik.icure.asyncdao.samv2.ProductIdDAO
import org.taktik.icure.asyncdao.samv2.SubstanceDAO
import org.taktik.icure.asyncdao.samv2.VerseDAO
import org.taktik.icure.asyncdao.samv2.VmpDAO
import org.taktik.icure.asyncdao.samv2.VmpGroupDAO
import org.taktik.icure.asynclogic.samv2.impl.SamV2LogicImpl
import org.taktik.icure.db.PaginationOffset
import org.taktik.icure.entities.samv2.Amp

@OptIn(ExperimentalCoroutinesApi::class)
class SamV2LogicImplTest {
	private val ampDAO: AmpDAO = mockk(relaxed = true)
	private val vmpDAO: VmpDAO = mockk(relaxed = true)
	private val vmpGroupDAO: VmpGroupDAO = mockk(relaxed = true)
	private val productIdDAO: ProductIdDAO = mockk(relaxed = true)
	private val nmpDAO: NmpDAO = mockk(relaxed = true)
	private val substanceDAO: SubstanceDAO = mockk(relaxed = true)
	private val pharmaceuticalFormDAO: PharmaceuticalFormDAO = mockk(relaxed = true)
	private val paragraphDAO: ParagraphDAO = mockk(relaxed = true)
	private val verseDAO: VerseDAO = mockk(relaxed = true)

	private val samV2Logic: SamV2Logic = SamV2LogicImpl(
		ampDAO,
		vmpDAO,
		vmpGroupDAO,
		productIdDAO,
		nmpDAO,
		substanceDAO,
		pharmaceuticalFormDAO,
		paragraphDAO,
		verseDAO
	)

	private val ampsNames = listOf("Dafalgan", "Ibuprofen")
	private val ampsKind = listOf("", "Effervescent")
	private val ampsStrength = listOf("10", "100", "50", "500", "600")

	private val amps = ampsNames.flatMap { name ->
		ampsKind.flatMap { kind ->
			ampsStrength.map { strength ->
				"$name $kind $strength mg"
			}
		}

	}.map {
		Amp(
			officialName = it,
			id = UUID.randomUUID().toString()
		)
	}

	@Test
	fun `find amps by label, should find all of Dafalgan`() {
		val search = "Dafalgan"
		val ampsIds = amps.filter { amp -> search.split(" ").all { amp.officialName!!.contains(it) }  }.map { it.id }

		coEvery { ampDAO.listAmpIdsByLabel(any(), search) } returns ampsIds.asFlow()
		coEvery { ampDAO.getEntities(any()) } returns amps.filter { it.id in ampsIds }.asFlow()

		runBlocking {
			val result = samV2Logic.findAmpsByLabel("fr", search, PaginationOffset(1000)).toList()
			assert(result.size == ampsIds.size)
			assert(result.all { it.officialName!!.contains("Dafalgan") })
			assert(result.none { it.officialName!!.contains("Ibuprofen") })
			assert(result.any { it.officialName!!.contains("Effervescent") })
		}
	}

	@Test
	fun `find amps by label, should find all of Effervescent`() {
		val search = "Effervescent"
		val ampsIds = amps.filter { amp -> search.split(" ").all { amp.officialName!!.contains(it) }  }.map { it.id }

		coEvery { ampDAO.listAmpIdsByLabel(any(), search) } returns ampsIds.asFlow()
		coEvery { ampDAO.getEntities(any()) } returns amps.filter { it.id in ampsIds }.asFlow()

		runBlocking {
			val result = samV2Logic.findAmpsByLabel("fr", search, PaginationOffset(1000)).toList()
			assert(result.size == ampsIds.size)
			assert(result.any { it.officialName!!.contains("Dafalgan") })
			assert(result.any { it.officialName!!.contains("Ibuprofen") })
			assert(result.all { it.officialName!!.contains("Effervescent") })
			assert(result.any { it.officialName!!.contains("50") })
			assert(result.any { it.officialName!!.contains("10") })
			assert(result.any { it.officialName!!.contains("600") })
		}
	}

	@Test
	fun `find amps by label, should find all of Dafalgan 50+`() {
		val search = "Dafalgan 50"
		val ampsIds = amps.filter { amp -> search.split(" ").all { amp.officialName!!.contains(it) }  }.map { it.id }
		coEvery { ampDAO.listAmpIdsByLabel(any(), search) } returns ampsIds.asFlow()
		coEvery { ampDAO.getEntities(any()) } returns amps.filter { it.id in ampsIds }.asFlow()

		runBlocking {
			val result = samV2Logic.findAmpsByLabel("fr", search, PaginationOffset(1000)).toList()
			assert(result.size == ampsIds.size)
			assert(result.any { it.officialName!!.contains("Dafalgan") })
			assert(result.none { it.officialName!!.contains("Ibuprofen") })
			assert(result.any { it.officialName!!.contains("Effervescent") })
			assert(result.all { it.officialName!!.contains("50") })
			assert(result.any { it.officialName!!.contains("500") })
			assert(result.none { it.officialName!!.contains("10") })
			assert(result.none { it.officialName!!.contains("600") })
		}
	}

	@Test
	fun `find amps by label, should find all of Ibuprofen Effervescent 10 mg`() {
		val search = "Ibuprofen Effervescent 10 mg"
		val ampsIds = amps.filter { amp -> search.split(" ").all { amp.officialName!!.contains(it) }  }.map { it.id }
		coEvery { ampDAO.listAmpIdsByLabel(any(), search) } returns ampsIds.asFlow()
		coEvery { ampDAO.getEntities(any()) } returns amps.filter { it.id in ampsIds }.asFlow()

		runBlocking {
			val result = samV2Logic.findAmpsByLabel("fr", search, PaginationOffset(1000)).toList()
			assert(result.size == ampsIds.size)
			assert(result.none { it.officialName!!.contains("Dafalgan") })
			assert(result.all { it.officialName!!.contains("Ibuprofen") })
			assert(result.all { it.officialName!!.contains("Effervescent") })
			assert(result.none { it.officialName!!.contains("50") })
			assert(result.all { it.officialName!!.contains("10") })
			assert(result.any { it.officialName!!.contains("100") })
			assert(result.none { it.officialName!!.contains("600") })
		}
	}
}
