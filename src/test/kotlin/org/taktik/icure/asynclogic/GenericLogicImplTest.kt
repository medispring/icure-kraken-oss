package org.taktik.icure.asynclogic

import java.lang.IllegalStateException
import java.util.UUID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.count
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.entities.MaintenanceTask
import org.taktik.icure.test.ICureTestApplication

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
class GenericLogicImplTest (
	taskLogic: MaintenanceTaskLogic
): StringSpec({

	"Can create some DTOs if all of them have the rev field equal to none" {
		val entities = listOf(
			MaintenanceTask(id = UUID.randomUUID().toString(), rev = null),
			MaintenanceTask(id = UUID.randomUUID().toString(), rev = null),
			MaintenanceTask(id = UUID.randomUUID().toString(), rev = null),
		)
		taskLogic.createEntities(entities).count() shouldBe 3
		taskLogic.getGenericDAO().getEntities(entities.map { it.id }).count() shouldBe 3
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
		taskLogic.getGenericDAO().getEntities(entities.map { it.id }).count() shouldBe 0
	}

})
