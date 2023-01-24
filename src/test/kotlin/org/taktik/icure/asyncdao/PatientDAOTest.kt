package org.taktik.icure.asyncdao

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.test.context.ActiveProfiles
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
class PatientDAOTest(
	@Autowired val patientDAO: PatientDAO
): StringSpec() {

	init {
		runBlocking {
			val resolver = PathMatchingResourcePatternResolver(javaClass.classLoader)
			testPatientFilter(patientDAO)
		}
	}
}

private suspend fun StringSpec.testPatientFilter(
 patientDAO: PatientDAO
){
	val postalCode = "9090"
	val houseNumber = "340"
	val streetAndCity = "brus"
	val hcpWithoutPatients = "f12d7fa2-e95f-45d7-ad7f-a2e95f75d763"
	val hcpWithPatients = "f6789440-7d67-4b11-b894-407d67ab112d"
	"If streetAndCity postalCode and houseNumber are null, but the HCP has no patients, then the result is empty"{
		val patientIds = patientDAO.listPatientIdsByHcPartyAndAddress(null,null, null, hcpWithoutPatients).toList()
		patientIds.size shouldBe 0
	}
	"If streetAndCity postalCode and houseNumber are null, then all the patients are returned for the specified HCP"{
		val patientIds = patientDAO.listPatientIdsByHcPartyAndAddress(null,null, null, hcpWithPatients).toList()
		patientIds.size shouldBeGreaterThan 0
	}
	"if streetAndCity and postalCode are null but houseNumber isn’t, then all the results have the correct house number"{

		val patientIds = patientDAO.listPatientIdsByHcPartyAndAddress(null, null, houseNumber, hcpWithPatients).toList()
		patientIds.size shouldBeGreaterThan 0
		val patients = patientDAO.getPatients(patientIds).toList()
		patients.forEach{patient ->
			patient.addresses.map { it.houseNumber } shouldContain houseNumber
		}
	}
	"if streetAndCity and houseNumber are null but postalCode isn’t, then all the results have the correct postal code"{
		val patientIds = patientDAO.listPatientIdsByHcPartyAndAddress(null, postalCode, null, hcpWithPatients).toList()
		patientIds.size shouldBeGreaterThan 0
		val patients = patientDAO.getPatients(patientIds).toList()
		patients.forEach{patient ->
			patient.addresses.map { it.postalCode } shouldContain postalCode
		}
	}
	"if streetAndCiy is null but houseNumber and postalCode aren’t, then all the results have the correct postal code and house number"{
		val patientIds = patientDAO.listPatientIdsByHcPartyAndAddress(null, postalCode, houseNumber, hcpWithPatients).toList()
		patientIds.size shouldBeGreaterThan 0
		val patients = patientDAO.getPatients(patientIds).toList()
		patients.forEach{patient ->
			patient.addresses.map { "${it.postalCode}/${it.houseNumber}" } shouldContain "$postalCode/$houseNumber"
		}
	}
	"if streetAndCity is not null, then all the results have the correct streetAndCity parameter"{
		val patientIds = patientDAO.listPatientIdsByHcPartyAndAddress(streetAndCity,null, null, hcpWithPatients).toList()
		patientIds.size shouldBeGreaterThan 0
		val patients = patientDAO.getPatients(patientIds).toList()
		patients.forEach{patient ->
			val cityCnt  = patient.addresses.filter { it.city?.contains(streetAndCity, ignoreCase = true) ?:  false}.size
			val streetCnt = patient.addresses.filter { it.street?.contains(streetAndCity, ignoreCase = true) ?: false}.size
			cityCnt + streetCnt shouldBeGreaterThan  0
		}
	}
	"if streetAndCity is blank, all the patients for that HCP are returned (I’m understanding that this is how it will behave)"{
		val patientIds = patientDAO.listPatientIdsByHcPartyAndAddress("",null, null, hcpWithPatients).toList()
		patientIds.size shouldBeGreaterThan 0
	}
	"if streetAndCity and houseNumber are not null but , then all the results are in the correct city and with the specified house number"{
		val patientIds = patientDAO.listPatientIdsByHcPartyAndAddress(streetAndCity, null, houseNumber, hcpWithPatients).toList()
		patientIds.size shouldBeGreaterThan 0
		val patients = patientDAO.getPatients(patientIds).toList()
		patients.forEach{patient ->
			patient.addresses.filter { address -> ((address.street?.contains(streetAndCity, ignoreCase = true) ?: false) || (address.city?.contains(streetAndCity, ignoreCase = true) ?: false)) && address.houseNumber == houseNumber }.size shouldBeGreaterThan 0
		}
	}
	"if all the parameters are not null, then all the results have the correct streetAndCity, postalCode and houseNumber"{
		val patientIds = patientDAO.listPatientIdsByHcPartyAndAddress(streetAndCity, postalCode, houseNumber, hcpWithPatients).toList()
		patientIds.size shouldBeGreaterThan 0
		val patients = patientDAO.getPatients(patientIds).toList()
		patients.forEach{patient ->
			patient.addresses.filter { address -> ((address.street?.contains(streetAndCity, ignoreCase = true) ?: false) || (address.city?.contains(streetAndCity, ignoreCase = true) ?: false)) && address.postalCode == postalCode && address.houseNumber == houseNumber }.size shouldBeGreaterThan 0
		}
	}

}
