package org.taktik.icure.asynclogic

import java.lang.IllegalStateException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.count
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.test.FakeDto
import org.taktik.icure.test.FakeLogicImpl
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
	fakeLogicImpl: FakeLogicImpl
): StringSpec({

	"Can create some DTOs if all of them have the rev field equal to none" {
		fakeLogicImpl.fakeDAO.resetCache()
		val entities = listOf(
			FakeDto(id = "FAKE1", rev = null, revHistory = null),
			FakeDto(id = "FAKE2", rev = null, revHistory = null),
			FakeDto(id = "FAKE3", rev = null, revHistory = null)
		)
		fakeLogicImpl.createEntities(entities).count() shouldBe 3
		fakeLogicImpl.fakeDAO.cachedEntities.count() shouldBe 3
	}

	"Creation of DTOs fails if at least onw of them has the rev field not equal to none" {
		fakeLogicImpl.fakeDAO.resetCache()
		val entities = listOf(
			FakeDto(id = "FAKE1", rev = null, revHistory = null),
			FakeDto(id = "FAKE2", rev = null, revHistory = null),
			FakeDto(id = "FAKE3", rev = "DEFINITELY_NOT_NULL", revHistory = null)
		)
		shouldThrow<IllegalStateException> {
			fakeLogicImpl.createEntities(entities).count()
		}
		fakeLogicImpl.fakeDAO.cachedEntities.count() shouldBe 0
	}

})
