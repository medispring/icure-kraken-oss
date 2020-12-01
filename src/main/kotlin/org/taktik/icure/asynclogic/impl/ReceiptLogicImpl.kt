package org.taktik.icure.asynclogic.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.stereotype.Service
import org.taktik.icure.asyncdao.ReceiptDAO
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.ReceiptLogic
import org.taktik.icure.entities.Receipt
import org.taktik.icure.entities.embed.ReceiptBlobType
import java.nio.ByteBuffer

@Service
class ReceiptLogicImpl(private val receiptDAO: ReceiptDAO,
                       private val sessionLogic: AsyncSessionLogic) : GenericLogicImpl<Receipt, ReceiptDAO>(sessionLogic), ReceiptLogic {

    @ExperimentalCoroutinesApi
    override fun listByReference(ref: String): Flow<Receipt> = flow {
        emitAll(receiptDAO.listByReference(ref))
    }

    @ExperimentalCoroutinesApi
    override fun getAttachment(receiptId: String, attachmentId: String): Flow<ByteBuffer> = flow {
        emitAll(receiptDAO.getAttachment(receiptId, attachmentId))
    }

    override suspend fun addReceiptAttachment(receipt: Receipt, blobType: ReceiptBlobType, payload: ByteArray) : Receipt {
        val newAttachmentId = DigestUtils.sha256Hex(payload)
        val modifiedReceipt = updateEntities(listOf(receipt.copy(attachmentIds = receipt.attachmentIds + (blobType to newAttachmentId)))).first()
        val contentType = "application/octet-stream"
        return modifiedReceipt.copy(rev = receiptDAO.createAttachment(modifiedReceipt.id, newAttachmentId, modifiedReceipt.rev ?: error("Invalid receipt : no rev"), contentType, flowOf(ByteBuffer.wrap(payload))))
    }

    override fun getGenericDAO(): ReceiptDAO {
        return receiptDAO
    }
}
