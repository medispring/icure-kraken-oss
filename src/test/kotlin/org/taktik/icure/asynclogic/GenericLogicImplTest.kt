package org.taktik.icure.asynclogic

import java.lang.IllegalStateException
import java.util.UUID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.asyncdao.MaintenanceTaskDAO
import org.taktik.icure.asynclogic.impl.MaintenanceTaskLogicImpl
import org.taktik.icure.entities.MaintenanceTask
import org.taktik.icure.asynclogic.impl.filter.Filters
import org.taktik.icure.test.ICureTestApplication

private const val TEST_CACHE = "build/tests/icureCache"

@OptIn(ExperimentalCoroutinesApi::class)
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
class GenericLogicImplTest : StringSpec({

	val mockDAO = mockk<MaintenanceTaskDAO> {
		coEvery { create(any<Collection<MaintenanceTask>>()) } answers { firstArg<Collection<MaintenanceTask>>().asFlow() }
	}
	val mockSessionLogic = mockk<AsyncSessionLogic>()
	val mockFilters = mockk<Filters>()
	val taskLogic = MaintenanceTaskLogicImpl(mockDAO, mockSessionLogic, mockFilters)

	"Can create some DTOs if all of them have the rev field equal to none" {
		val entities = listOf(
			MaintenanceTask(id = UUID.randomUUID().toString(), rev = null),
			MaintenanceTask(id = UUID.randomUUID().toString(), rev = null),
			MaintenanceTask(id = UUID.randomUUID().toString(), rev = null),
		)
		taskLogic.createEntities(entities).count() shouldBe 3
	}

	"Creation of DTOs fails if at least one of them has the rev field not equal to none" {
		val entities = listOf(
			MaintenanceTask(id = UUID.randomUUID().toString(), rev = null),
			MaintenanceTask(id = UUID.randomUUID().toString(), rev = null),
			MaintenanceTask(id = UUID.randomUUID().toString(), rev = "DEFINITELY_NOT_NULL")
		)
		shouldThrow<IllegalStateException> {
			taskLogic.createEntities(entities).count()
		}
	}

})
