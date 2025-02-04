/*
 *  iCure Data Stack. Copyright (c) 2020 Taktik SA
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful, but
 *     WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public
 *     License along with this program.  If not, see
 *     <https://www.gnu.org/licenses/>.
 */
package org.taktik.icure.asynclogic.impl

import java.nio.ByteBuffer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import org.springframework.stereotype.Service
import org.taktik.couchdb.entity.Option
import org.taktik.icure.asyncdao.DocumentDAO
import org.taktik.icure.asynclogic.DocumentLogic
import org.taktik.icure.asynclogic.objectstorage.DataAttachmentModificationLogic.DataAttachmentChange
import org.taktik.icure.asynclogic.objectstorage.DocumentDataAttachmentModificationLogic
import org.taktik.icure.entities.Document

@ExperimentalCoroutinesApi
@Service
class DocumentLogicImpl(
	private val documentDAO: DocumentDAO,
	sessionLogic: AsyncSessionLogicImpl,
	private val attachmentModificationLogic: DocumentDataAttachmentModificationLogic
) : GenericLogicImpl<Document, DocumentDAO>(sessionLogic), DocumentLogic {

	override suspend fun createDocument(document: Document, strict: Boolean) = fix(document) { fixedDocument ->
		documentDAO.create(checkNewDocument(fixedDocument, strict))
	}

	override suspend fun getDocument(documentId: String): Document? {
		return documentDAO.get(documentId)
	}

	override suspend fun getDocumentsByExternalUuid(documentId: String): List<Document> {
		return documentDAO.listDocumentsByExternalUuid(documentId)
	}

	override fun getDocuments(documentIds: List<String>) = documentDAO.getEntities(documentIds)

	override fun getAttachment(documentId: String, attachmentId: String): Flow<ByteBuffer> = flow {
		emitAll(documentDAO.readAttachment(documentId, attachmentId, null))
	}

	override suspend fun modifyDocument(updatedDocument: Document, currentDocument: Document?, strict: Boolean): Document? = fix(updatedDocument) { newDoc ->
		val baseline = requireNotNull(currentDocument ?: getDocument(newDoc.id)) {
			"Attempting to modify a non-existing document ${newDoc.id}."
		}
		require(newDoc.rev == baseline.rev) { "Updated document has an older revision ${newDoc.rev} -> ${baseline.rev}" }
		documentDAO.save(
			newDoc
				.copy(attachments = baseline.attachments)
				.let {
					attachmentModificationLogic.ensureValidAttachmentChanges(
						baseline,
						it,
						if (strict) emptySet() else setOf(updatedDocument.mainAttachmentKey)
					)
				}
		)
	}

	override fun createOrModifyDocuments(documents: List<DocumentLogic.BatchUpdateDocumentInfo>, strict: Boolean): Flow<Document> = flow {
		val fixedDocuments = documents.map { (newDoc, prevDoc) ->
			if (prevDoc != null) {
				fix(newDoc)
					.copy(attachments = prevDoc.attachments)
					.let {
						attachmentModificationLogic.ensureValidAttachmentChanges(
							prevDoc,
							it,
							if (strict) emptySet() else setOf(newDoc.mainAttachmentKey)
						)
					}
			} else {
				checkNewDocument(fix(newDoc), strict)
			}
		}
		emitAll(documentDAO.save(fixedDocuments))
	}


	override suspend fun updateAttachments(
		currentDocument: Document,
		mainAttachmentChange: DataAttachmentChange?,
		secondaryAttachmentsChanges: Map<String, DataAttachmentChange>
	): Document? =
		attachmentModificationLogic.updateAttachments(
			currentDocument,
			mainAttachmentChange?.let {
				if (it is DataAttachmentChange.CreateOrUpdate && it.utis == null && currentDocument.mainAttachment == null) {
					// Capture cases where the document has no attachment id set (main attachment is null) but specifies some utis
					it.copy(utis = listOfNotNull(currentDocument.mainUti) + currentDocument.otherUtis)
				} else it
			}?.let {
				secondaryAttachmentsChanges + (currentDocument.mainAttachmentKey to it)
			} ?: secondaryAttachmentsChanges
		)

	override fun listDocumentsByDocumentTypeHCPartySecretMessageKeys(documentTypeCode: String, hcPartyId: String, secretForeignKeys: List<String>): Flow<Document> = flow {
		emitAll(documentDAO.listDocumentsByDocumentTypeHcPartySecretMessageKeys(documentTypeCode, hcPartyId, secretForeignKeys))
	}

	override fun listDocumentsByHCPartySecretMessageKeys(hcPartyId: String, secretForeignKeys: List<String>): Flow<Document> = flow {
		emitAll(documentDAO.listDocumentsByHcPartyAndSecretMessageKeys(hcPartyId, secretForeignKeys))
	}

	override fun listDocumentsWithoutDelegation(limit: Int): Flow<Document> = flow {
		emitAll(documentDAO.listDocumentsWithNoDelegations(limit))
	}

	override fun solveConflicts(ids: List<String>?): Flow<Document> {
		val documentsInConflict = ids?.asFlow()?.mapNotNull { documentDAO.get(it, Option.CONFLICTS) }
			?: documentDAO.listConflicts().mapNotNull { documentDAO.get(it.id, Option.CONFLICTS) }
		return documentsInConflict.mapNotNull {
			documentDAO.get(it.id, Option.CONFLICTS)?.let { document ->
				document.conflicts?.mapNotNull { conflictingRevision -> documentDAO.get(document.id, conflictingRevision) }
					?.fold(document) { kept, conflict -> kept.merge(conflict).also { documentDAO.purge(conflict) } }
					?.let { mergedDocument -> documentDAO.save(mergedDocument) }
			}
		}
	}

	override fun getGenericDAO(): DocumentDAO {
		return documentDAO
	}

	private fun checkNewDocument(document: Document, strict: Boolean): Document {
		require(document.secondaryAttachments.isEmpty()) {
			"New document can't provide any secondary attachments information."
		}
		require(document.deletedAttachments.isEmpty()) {
			"New document can't specify deleted attachments."
		}
		if (strict) {
			require(document.mainAttachment == null && document.mainUti == null && document.otherUtis.isEmpty()) {
				"New document can't specify any main attachment information"
			}
		}
		require(document.objectStoreReference == null) {
			"New document can't specify a value for the main attachment object store id."
		}
		return if (document.attachmentId != null) document.copy(attachmentId = null) else document
	}
}
