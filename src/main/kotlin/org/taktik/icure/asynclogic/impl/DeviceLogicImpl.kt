package org.taktik.icure.asynclogic.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.taktik.couchdb.DocIdentifier
import org.taktik.couchdb.ViewQueryResultEvent
import org.taktik.icure.asyncdao.DeviceDAO
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.DeviceLogic
import org.taktik.icure.asynclogic.UserLogic
import org.taktik.icure.asynclogic.impl.filter.Filters
import org.taktik.icure.db.PaginationOffset
import org.taktik.icure.domain.filter.chain.FilterChain
import org.taktik.icure.entities.Device

@FlowPreview
@ExperimentalCoroutinesApi
@Service
class DeviceLogicImpl(private val sessionLogic: AsyncSessionLogic,
                      private val deviceDAO: DeviceDAO,
                      private val userLogic: UserLogic,
                      private val filters: Filters): DeviceLogic {

    override suspend fun createDevice(device: Device): Device? {
        TODO("Not yet implemented")
    }

    override fun createDevices(devices: List<Device>): Flow<Device> {
        TODO("Not yet implemented")
    }

    override suspend fun modifyDevice(device: Device): Device? {
        TODO("Not yet implemented")
    }

    override fun modifyDevices(devices: List<Device>): Flow<Device> {
        TODO("Not yet implemented")
    }

    override suspend fun getDevice(deviceId: String): Device? {
        TODO("Not yet implemented")
    }

    override fun getDevices(deviceIds: List<String>): Flow<Device> {
        TODO("Not yet implemented")
    }

    override fun deleteDevices(ids: Collection<String>): Flow<DocIdentifier> {
        TODO("Not yet implemented")
    }

    override fun listDevices(paginationOffset: PaginationOffset<*>?, filterChain: FilterChain<Device>, sort: String?, desc: Boolean?): Flow<ViewQueryResultEvent> {
        TODO("Not yet implemented")
    }
}
