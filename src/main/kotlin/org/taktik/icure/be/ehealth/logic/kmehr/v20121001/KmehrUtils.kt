package org.taktik.icure.be.ehealth.logic.kmehr.v20121001

import org.taktik.icure.be.ehealth.dto.kmehr.v20121001.be.fgov.ehealth.standards.kmehr.cd.v1.CDSEXvalues

fun String.toSEXvalues(): CDSEXvalues = when (this) {
	"changedToMale", "changedToFemale" -> CDSEXvalues.CHANGED
	"indeterminate" -> CDSEXvalues.UNKNOWN
	else -> CDSEXvalues.fromValue(this)
}
