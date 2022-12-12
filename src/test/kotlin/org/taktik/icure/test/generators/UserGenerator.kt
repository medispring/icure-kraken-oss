package org.taktik.icure.test.generators

import org.taktik.icure.services.external.rest.v1.dto.UserDto
import org.taktik.icure.test.generateRandomString

class UserGenerator {

	private val alphabet: List<Char> = ('a'..'z').toList() + ('A'..'Z') + ('0'..'9')

	fun generateRandomUsers(num: Int) = List(num) {
        UserDto(
            id = generateRandomString(20, alphabet),
            patientId = generateRandomString(10, alphabet),
            login = generateRandomString(5, alphabet) + '@' + generateRandomString(5, alphabet)
        )
	}
}
