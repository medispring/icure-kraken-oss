package org.taktik.icure.test.fakedaos

import java.net.URI
import kotlinx.coroutines.flow.Flow
import org.taktik.couchdb.ViewQueryResultEvent
import org.taktik.couchdb.entity.ComplexKey
import org.taktik.icure.asyncdao.GenericDAO
import org.taktik.icure.asyncdao.PatientDAO
import org.taktik.icure.db.PaginationOffset
import org.taktik.icure.entities.Patient
import org.taktik.icure.entities.embed.Gender
import org.taktik.icure.entities.embed.Identifier
import org.taktik.icure.test.FakeGenericDAO

class FakePatientDAO : PatientDAO, GenericDAO<Patient> by FakeGenericDAO() {
	// Implement as needed by tests

	override fun listPatientIdsByHcPartyAndName(name: String, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsOfHcPartyAndName(name: String, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcPartyAndSsin(ssin: String, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsOfHcPartyAndSsin(ssin: String, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByActive(active: Boolean, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listOfMergesAfter(date: Long?): Flow<Patient> {
		TODO("Not yet implemented")
	}

	override suspend fun countByHcParty(healthcarePartyId: String): Int {
		TODO("Not yet implemented")
	}

	override suspend fun countOfHcParty(healthcarePartyId: String): Int {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcParty(healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcPartyAndDateOfBirth(date: Int?, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcPartyAndDateOfBirth(startDate: Int?, endDate: Int?, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcPartyGenderEducationProfession(healthcarePartyId: String, gender: Gender?, education: String?, profession: String?): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsForHcPartyDateOfBirth(date: Int?, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcPartyAndNameContainsFuzzy(searchString: String?, healthcarePartyId: String, limit: Int?): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsOfHcPartyNameContainsFuzzy(searchString: String?, healthcarePartyId: String, limit: Int?): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcPartyAndExternalId(externalId: String?, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcPartyAndTelecom(searchString: String?, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcPartyAndAddress(searchString: String?, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun findPatientIdsByHcParty(healthcarePartyId: String, pagination: PaginationOffset<ComplexKey>): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findPatientsByHcPartyAndName(name: String?, healthcarePartyId: String, pagination: PaginationOffset<ComplexKey>, descending: Boolean): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findPatientsOfHcPartyAndName(name: String?, healthcarePartyId: String, offset: PaginationOffset<ComplexKey>, descending: Boolean): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findPatientsByHcPartyAndSsin(ssin: String?, healthcarePartyId: String, pagination: PaginationOffset<ComplexKey>, descending: Boolean): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findPatientsOfHcPartyAndSsin(ssin: String?, healthcarePartyId: String, offset: PaginationOffset<ComplexKey>, descending: Boolean): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findPatientsByHcPartyModificationDate(startDate: Long?, endDate: Long?, healthcarePartyId: String, pagination: PaginationOffset<ComplexKey>, descending: Boolean): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findPatientsOfHcPartyModificationDate(startDate: Long?, endDate: Long?, healthcarePartyId: String, pagination: PaginationOffset<ComplexKey>, descending: Boolean): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findPatientsByHcPartyDateOfBirth(startDate: Int?, endDate: Int?, healthcarePartyId: String, pagination: PaginationOffset<ComplexKey>, descending: Boolean): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findPatientsOfHcPartyDateOfBirth(startDate: Int?, endDate: Int?, healthcarePartyId: String, pagination: PaginationOffset<ComplexKey>, descending: Boolean): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override suspend fun findPatientsByUserId(id: String): Patient? {
		TODO("Not yet implemented")
	}

	override fun getPatients(patIds: Collection<String>): Flow<Patient> =
		getEntities(patIds)

	override suspend fun getPatientByExternalId(externalId: String): Patient? {
		TODO("Not yet implemented")
	}

	override fun findDeletedPatientsByDeleteDate(start: Long, end: Long?, descending: Boolean, paginationOffset: PaginationOffset<Long>): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findDeletedPatientsByNames(firstName: String?, lastName: String?): Flow<Patient> {
		TODO("Not yet implemented")
	}

	override fun listConflicts(): Flow<Patient> {
		TODO("Not yet implemented")
	}

	override fun findPatientsModifiedAfter(date: Long, paginationOffset: PaginationOffset<Long>): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcPartyAndSsins(ssins: Collection<String>, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientsByHcPartyName(searchString: String?, healthcarePartyId: String): Flow<String> {
		TODO("Not yet implemented")
	}

	override suspend fun getHcPartyKeysForDelegate(healthcarePartyId: String): Map<String, String> {
		TODO("Not yet implemented")
	}

	override suspend fun getAesExchangeKeysForDelegate(healthcarePartyId: String): Map<String, Map<String, Map<String, String>>> {
		TODO("Not yet implemented")
	}

	override fun getDuplicatePatientsBySsin(healthcarePartyId: String, paginationOffset: PaginationOffset<ComplexKey>): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun getDuplicatePatientsByName(healthcarePartyId: String, paginationOffset: PaginationOffset<ComplexKey>): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findPatients(ids: Collection<String>): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findPatients(ids: Flow<String>): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun listPatientIdsByHcPartyAndIdentifiers(healthcarePartyId: String, identifiers: List<Identifier>): Flow<String> {
		TODO("Not yet implemented")
	}

	override fun listPatientsByHcPartyAndIdentifier(healthcarePartyId: String, system: String, id: String): Flow<Patient> {
		TODO("Not yet implemented")
	}
}
