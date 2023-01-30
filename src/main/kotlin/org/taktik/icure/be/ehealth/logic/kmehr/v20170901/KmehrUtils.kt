package org.taktik.icure.be.ehealth.logic.kmehr.v20170901

import org.taktik.icure.be.ehealth.dto.kmehr.v20170901.be.fgov.ehealth.standards.kmehr.cd.v1.CDSEXvalues

fun String.toSEXvalues(): CDSEXvalues = when (this) {
	"changedToMale", "changedToFemale" -> CDSEXvalues.CHANGED
	"indeterminate" -> CDSEXvalues.UNDEFINED
	else -> CDSEXvalues.fromValue(this)
}
