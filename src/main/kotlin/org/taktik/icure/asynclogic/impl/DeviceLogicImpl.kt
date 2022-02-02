package org.taktik.icure.asynclogic.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.taktik.couchdb.DocIdentifier
import org.taktik.icure.asyncdao.DeviceDAO
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.DeviceLogic
import org.taktik.icure.asynclogic.UserLogic
import org.taktik.icure.asynclogic.impl.filter.Filters
import org.taktik.icure.entities.Device

@FlowPreview
@ExperimentalCoroutinesApi
@Service
class DeviceLogicImpl(
    private val sessionLogic: AsyncSessionLogic,
    private val deviceDAO: DeviceDAO,
    private val userLogic: UserLogic,
    private val filters: Filters
) : GenericLogicImpl<Device, DeviceDAO>(sessionLogic), DeviceLogic {

    override suspend fun createDevice(device: Device): Device? = fix(device) { fixedDevice ->
        try {
            createEntities(setOf(fixedDevice)).firstOrNull()
        } catch (e: Exception) {
            log.error("createDevice: " + e.message)
            throw IllegalArgumentException("Invalid Device problem", e)
        }
    }

    override fun createDevices(devices: List<Device>): Flow<Device> = flow {
        try {
            emitAll(createEntities(devices.map { device -> fix(device) }))
        } catch (e: Exception) {
            log.error("createDevice: " + e.message)
            throw IllegalArgumentException("Invalid Device problem", e)
        }
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

    companion object {
        private val log = LoggerFactory.getLogger(DeviceLogicImpl::class.java)
    }

    override fun getGenericDAO(): DeviceDAO {
        return deviceDAO
    }
}
