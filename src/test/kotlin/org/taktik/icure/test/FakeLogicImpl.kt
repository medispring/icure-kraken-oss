package org.taktik.icure.test

import org.springframework.stereotype.Service
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.impl.GenericLogicImpl

@Service
class FakeLogicImpl(
	sessionLogic: AsyncSessionLogic
) : GenericLogicImpl<FakeDto, FakeDAOImpl>(sessionLogic) {

	val fakeDAO = FakeDAOImpl()

	override fun getGenericDAO(): FakeDAOImpl {
		return fakeDAO
	}

}
