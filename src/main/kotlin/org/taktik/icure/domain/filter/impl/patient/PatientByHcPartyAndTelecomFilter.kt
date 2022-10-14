package org.taktik.icure.domain.filter.impl.patient

import com.github.pozo.KotlinBuilder
import org.taktik.icure.domain.filter.AbstractFilter
import org.taktik.icure.entities.Patient

@KotlinBuilder
data class PatientByHcPartyAndTelecomFilter(
	override val desc: String? = null,
	override val searchString: String? = null,
	override val healthcarePartyId: String? = null
) : AbstractFilter<Patient>, org.taktik.icure.domain.filter.patient.PatientByHcPartyAndTelecomFilter {

	override fun matches(item: Patient): Boolean {
		return (healthcarePartyId == null || item.delegations.keys.contains(healthcarePartyId)) && (searchString == null || item.addresses.any {adr -> adr.telecoms.any { tc -> tc.telecomNumber?.contains(searchString)!! } }  )
	}
}
