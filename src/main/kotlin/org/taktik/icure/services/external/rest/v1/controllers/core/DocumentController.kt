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

package org.taktik.icure.services.external.rest.v1.controllers.core

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.mono
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.Part
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.taktik.icure.asynclogic.DocumentLogic
import org.taktik.icure.asynclogic.objectstorage.DataAttachmentModificationLogic.DataAttachmentChange
import org.taktik.icure.asynclogic.objectstorage.DocumentDataAttachmentLoader
import org.taktik.icure.asynclogic.objectstorage.contentFlowOfNullable
import org.taktik.icure.entities.Document
import org.taktik.icure.entities.embed.DocumentType
import org.taktik.icure.asynclogic.objectstorage.ObjectStorageException
import org.taktik.icure.security.CryptoUtils
import org.taktik.icure.security.CryptoUtils.isValidAesKey
import org.taktik.icure.security.CryptoUtils.tryKeyFromHexString
import org.taktik.icure.services.external.rest.v1.dto.DocumentDto
import org.taktik.icure.services.external.rest.v1.dto.IcureStubDto
import org.taktik.icure.services.external.rest.v1.dto.ListOfIdsDto
import org.taktik.icure.services.external.rest.v1.dto.requests.BulkAttachmentUpdateOptions
import org.taktik.icure.services.external.rest.v1.mapper.DocumentMapper
import org.taktik.icure.services.external.rest.v1.mapper.StubMapper
import org.taktik.icure.services.external.rest.v1.mapper.embed.DelegationMapper
import org.taktik.icure.utils.injectReactorContext
import org.taktik.icure.utils.toByteArray
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@OptIn(ExperimentalCoroutinesApi::class)
@RestController
@RequestMapping("/rest/v1/document")
@Tag(name = "document")
class DocumentController(
	private val documentLogic: DocumentLogic,
	private val documentMapper: DocumentMapper,
	private val delegationMapper: DelegationMapper,
	private val stubMapper: StubMapper,
	private val attachmentLoader: DocumentDataAttachmentLoader
) {
	@Operation(summary = "Create a document", description = "Creates a document and returns an instance of created document afterward")
	@PostMapping
	fun createDocument(@RequestBody documentDto: DocumentDto): Mono<DocumentDto> = mono {
		val document = documentMapper.map(documentDto)
		val createdDocument = documentLogic.createDocument(document, false)
			?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Document creation failed")
		documentMapper.map(createdDocument)
	}

	@Operation(summary = "Delete a document", description = "Deletes a batch of documents and returns the list of deleted document ids")
	@DeleteMapping("/{documentIds}")
	fun deleteDocument(@PathVariable documentIds: String) = flow {
		val documentIdsList = documentIds.split(',')
		try {
			emitAll(documentLogic.deleteEntities(documentIdsList))
		} catch (e: Exception) {
			throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Document deletion failed")
		}
	}.injectReactorContext()

	@Operation(summary = "Load a document's main attachment", responses = [ApiResponse(responseCode = "200", content = [Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = Schema(type = "string", format = "binary"))])])
	@GetMapping("/{documentId}/attachment/{attachmentId}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
	fun getDocumentAttachment(
		@PathVariable documentId: String,
		@PathVariable attachmentId: String,
		@RequestParam(required = false) enckeys: String?,
		@RequestParam(required = false) fileName: String?,
		response: ServerHttpResponse
	) = getDocumentAttachment(documentId, enckeys, fileName, response)

	@Operation(summary = "Load a document's main attachment", responses = [ApiResponse(responseCode = "200", content = [Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = Schema(type = "string", format = "binary"))])])
	@GetMapping("/{documentId}/attachment", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
	fun getDocumentAttachment(
		@PathVariable documentId: String,
		@RequestParam(required = false) enckeys: String?,
		@RequestParam(required = false) fileName: String?,
		response: ServerHttpResponse
	) = response.writeWith(
		flow {
			val document = documentLogic.getOr404(documentId)
			val attachment =
				if (enckeys.isNullOrBlank()) {
					attachmentLoader.contentFlowOfNullable(document, Document::mainAttachment)
				} else {
					attachmentLoader.decryptMainAttachment(document, enckeys)?.let { flowOf(DefaultDataBufferFactory.sharedInstance.wrap(it)) }
				} ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "AttachmentDto not found")

			response.headers["Content-Type"] = document.mainAttachment?.mimeType ?: "application/octet-stream"
			response.headers["Content-Disposition"] = "attachment; filename=\"${fileName ?: document.name}\""

			emitAll(attachment)
		}.injectReactorContext()
	)

	@Operation(summary = "Delete a document's main attachment", description = "Deletes the main attachment of a document and returns the modified document instance afterward")
	@DeleteMapping("/{documentId}/attachment")
	fun deleteAttachment(
		@PathVariable
		documentId: String,
		@Parameter(description = "Revision of the latest known version of the document. If provided the method will fail with a CONFLICT status code if the current version does not have this revision")
		@RequestParam(required = false)
		rev: String?
	): Mono<DocumentDto> = mono {
		val document = documentLogic.getOr404(documentId)
		checkRevision(rev, document)
		if (document.mainAttachment != null) {
			documentLogic.updateAttachments(
				document,
				mainAttachmentChange = DataAttachmentChange.Delete
			).let { documentMapper.map(checkNotNull(it) { "Failed to update attachment" }) }
		} else documentMapper.map(document)
	}

	@Operation(summary = "Creates or modifies a document's attachment", description = "Creates a document's attachment and returns the modified document instance afterward")
	@PutMapping("/{documentId}/attachment", consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
	fun setDocumentAttachment(
		@PathVariable
		documentId: String,
		@RequestParam(required = false)
		enckeys: String?,
		@Parameter(description = "Revision of the latest known version of the document. If provided the method will fail with a CONFLICT status code if the current version does not have this revision")
		@RequestParam(required = false)
		rev: String?,
		@RequestParam(required = false)
		@Parameter(description = "Utis for the attachment")
		utis: List<String>?,
		@Schema(type = "string", format = "binary")
		@RequestBody
		payload: Flow<DataBuffer>,
		@RequestHeader(name = HttpHeaders.CONTENT_LENGTH, required = false)
		lengthHeader: Long?
	): Mono<DocumentDto> = doSetDocumentAttachment(documentId, enckeys, rev, utis, lengthHeader?.takeIf { it > 0 }, payload)

	@Operation(summary = "Create or modifies a document's attachment", description = "Creates a document attachment and returns the modified document instance afterward")
	@PutMapping("/attachment", consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
	fun setDocumentAttachmentBody(
		@RequestParam(required = true)
		documentId: String,
		@RequestParam(required = false)
		enckeys: String?,
		@Parameter(description = "Revision of the latest known version of the document. If provided the method will fail with a CONFLICT status code if the current version does not have this revision")
		@RequestParam(required = false)
		rev: String?,
		@RequestParam(required = false)
		@Parameter(description = "Utis for the attachment")
		utis: List<String>?,
		@Schema(type = "string", format = "binary")
		@RequestBody
		payload: Flow<DataBuffer>,
		@RequestHeader(name = HttpHeaders.CONTENT_LENGTH, required = false)
		lengthHeader: Long?
	): Mono<DocumentDto> = doSetDocumentAttachment(documentId, enckeys, rev, utis, lengthHeader?.takeIf { it > 0 }, payload)

	@Operation(summary = "Creates or modifies a document's attachment", description = "Creates a document attachment and returns the modified document instance afterward")
	@PutMapping("/{documentId}/attachment/multipart", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
	fun setDocumentAttachmentMulti(
		@PathVariable
		documentId: String,
		@RequestParam(required = false)
		enckeys: String?,
		@Parameter(description = "Revision of the latest known version of the document. If provided the method will fail with a CONFLICT status code if the current version does not have this revision")
		@RequestParam(required = false)
		rev: String?,
		@RequestParam(required = false)
		@Parameter(description = "Utis for the attachment")
		utis: List<String>?,
		@RequestPart("attachment")
		payload: Part,
	): Mono<DocumentDto> = doSetDocumentAttachment(
		documentId,
		enckeys,
		rev,
		utis,
		payload.headers().contentLength.takeIf { it > 0 },
		payload.also {
			require(it.headers().contentType != null) {
				"attachment part must specify ${HttpHeaders.CONTENT_TYPE} header."
			}
		}.content().asFlow()
	)

	private fun doSetDocumentAttachment(
		documentId: String,
		enckeys: String?,
		rev: String?,
		utis: List<String>?,
		size: Long?,
		payload: Flow<DataBuffer>
	): Mono<DocumentDto> = mono {
		val validEncryptionKeys = enckeys
			?.takeIf { it.isNotEmpty() }
			?.split(',')
			?.mapNotNull { sfk -> sfk.tryKeyFromHexString()?.takeIf { it.isValidAesKey() } }
		if (enckeys != null && validEncryptionKeys.isNullOrEmpty()) throw ResponseStatusException(
			HttpStatus.BAD_REQUEST,
			"`enckeys` must contain at least a valid aes key"
		)
		val (newPayload, newSize) =
			if (validEncryptionKeys?.isNotEmpty() == true) Pair(
				// Encryption should never fail if the key is valid
				CryptoUtils.encryptFlowAES(payload, validEncryptionKeys.first())
					.map { DefaultDataBufferFactory.sharedInstance.wrap(it) },
				size?.let { CryptoUtils.predictAESEncryptedSize(it) }
			) else (payload to size)
		val document = documentLogic.getOr404(documentId)
		checkRevision(rev, document)
		val mainAttachmentChange =
			if (newSize != null)
				DataAttachmentChange.CreateOrUpdate(newPayload, newSize, utis)
			else
				newPayload.toByteArray(true).let { payloadBytes ->
					DataAttachmentChange.CreateOrUpdate(
						flowOf(DefaultDataBufferFactory.sharedInstance.wrap(payloadBytes)),
						payloadBytes.size.toLong(),
						utis
					)
				}
		documentLogic.updateAttachmentsWrappingExceptions(
			document,
			mainAttachmentChange = mainAttachmentChange
		)?.let { documentMapper.map(it) }
	}

	@Operation(summary = "Get a document", description = "Returns the document corresponding to the identifier passed in the request")
	@GetMapping("/{documentId}")
	fun getDocument(@PathVariable documentId: String): Mono<DocumentDto> = mono {
		val document = documentLogic.getOr404(documentId)
		documentMapper.map(document)
	}

	@Operation(summary = "Get a document", description = "Returns the first document corresponding to the externalUuid passed in the request")
	@GetMapping("/externaluuid/{externalUuid}")
	fun getDocumentByExternalUuid(@PathVariable externalUuid: String) = mono {
		val document = documentLogic.getDocumentsByExternalUuid(externalUuid).sortedByDescending { it.version }.firstOrNull()
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found")
		documentMapper.map(document)
	}

	@Operation(summary = "Get all documents with externalUuid", description = "Returns a list of document corresponding to the externalUuid passed in the request")
	@GetMapping("/externaluuid/{externalUuid}/all")
	fun getDocumentsByExternalUuid(@PathVariable externalUuid: String) = mono {
		documentLogic.getDocumentsByExternalUuid(externalUuid).map { documentMapper.map(it) }
	}

	@Operation(summary = "Get a batch of document", description = "Returns a list of document corresponding to the identifiers passed in the body")
	@PostMapping("/batch")
	fun getDocuments(@RequestBody documentIds: ListOfIdsDto): Flux<DocumentDto> {
		val documents = documentLogic.getDocuments(documentIds.ids)
		return documents.map { doc -> documentMapper.map(doc) }.injectReactorContext()
	}

	@Operation(summary = "Update a document", description = "Updates the document and returns an instance of the modified document afterward")
	@PutMapping
	fun modifyDocument(@RequestBody documentDto: DocumentDto): Mono<DocumentDto> = mono {
		val prevDoc = documentLogic.getDocument(documentDto.id)
		val newDocument = documentMapper.map(documentDto)
		(
			if (prevDoc == null) {
				documentLogic.createDocument(newDocument.copy(rev = null), false)
			} else if (prevDoc.attachmentId != newDocument.attachmentId) {
				documentLogic.modifyDocument(newDocument,  prevDoc, false)?.let {
					documentLogic.updateAttachments(it, mainAttachmentChange = DataAttachmentChange.Delete)
				}
			} else {
				documentLogic.modifyDocument(newDocument, prevDoc, false)
			}
		)?.let {
			documentMapper.map(it)
		} ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Document modification failed")
	}

	@Operation(summary = "Update a batch of documents", description = "Returns the modified documents.")
	@PutMapping("/batch")
	fun modifyDocuments(@RequestBody documentDtos: List<DocumentDto>): Flux<DocumentDto> = flow {
		val previousDocumentsById = documentLogic.getDocuments(documentDtos.map { it.id }).toList().associateBy { it.id }
		val allNewDocuments = documentDtos.map { documentMapper.map(it) }
		val newDocumentsById = allNewDocuments.associateBy { it.id }
		require(newDocumentsById.size == allNewDocuments.size) {
			"Provided documents can't have duplicate ids"
		}
		documentLogic.createOrModifyDocuments(
			allNewDocuments.map { DocumentLogic.BatchUpdateDocumentInfo(it, previousDocumentsById[it.id]) },
			false
		).map {
			val prev = previousDocumentsById[it.id]
			val curr = newDocumentsById.getValue(it.id)
			if (prev != null && prev.attachmentId != curr.attachmentId) {
				// No support for batch attachment update of different documents
				documentLogic.updateAttachments(it, mainAttachmentChange = DataAttachmentChange.Delete) ?: it
			} else it
		}.map {
			documentMapper.map(it)
		}.collect { emit(it) }
	}.injectReactorContext()

	@Operation(summary = "List documents found By Healthcare Party and secret foreign keys.", description = "Keys must be delimited by coma")
	@GetMapping("/byHcPartySecretForeignKeys")
	fun findDocumentsByHCPartyPatientForeignKeys(
		@RequestParam hcPartyId: String,
		@RequestParam secretFKeys: String
	): Flux<DocumentDto> {

		val secretMessageKeys = secretFKeys.split(',').map { it.trim() }
		val documentList = documentLogic.listDocumentsByHCPartySecretMessageKeys(hcPartyId, secretMessageKeys)
		return documentList.map { document -> documentMapper.map(document) }.injectReactorContext()
	}

	@Operation(summary = "List documents found By Healthcare Party and secret foreign keys.")
	@PostMapping("/byHcPartySecretForeignKeys")
	fun findDocumentsByHCPartyPatientForeignKeys(
		@RequestParam hcPartyId: String,
		@RequestBody secretMessageKeys: List<String>,
	): Flux<DocumentDto> {
		val documentList = documentLogic.listDocumentsByHCPartySecretMessageKeys(hcPartyId, secretMessageKeys)
		return documentList.map { document -> documentMapper.map(document) }.injectReactorContext()
	}

	@Operation(summary = "List documents found By type, By Healthcare Party and secret foreign keys.", description = "Keys must be delimited by coma")
	@GetMapping("/byTypeHcPartySecretForeignKeys")
	fun findByTypeHCPartyMessageSecretFKeys(
		@RequestParam documentTypeCode: String,
		@RequestParam hcPartyId: String,
		@RequestParam secretFKeys: String
	): Flux<DocumentDto> {
		if (DocumentType.fromName(documentTypeCode) == null) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid documentTypeCode.")
		}

		val secretMessageKeys = secretFKeys.split(',').map { it.trim() }
		val documentList = documentLogic.listDocumentsByDocumentTypeHCPartySecretMessageKeys(documentTypeCode, hcPartyId, secretMessageKeys)

		return documentList.map { document -> documentMapper.map(document) }.injectReactorContext()
	}

	@Operation(summary = "List documents with no delegation", description = "Keys must be delimited by coma")
	@GetMapping("/woDelegation")
	fun findWithoutDelegation(@RequestParam(required = false) limit: Int?): Flux<DocumentDto> {
		val documentList = documentLogic.listDocumentsWithoutDelegation(limit ?: 100)
		return documentList.map { document -> documentMapper.map(document) }.injectReactorContext()
	}

	@Operation(summary = "Update delegations in healthElements.", description = "Keys must be delimited by coma")
	@PostMapping("/delegations")
	fun setDocumentsDelegations(@RequestBody stubs: List<IcureStubDto>) = flow {
		val stubsById = stubs.associateBy { it.id }
		val invoices = documentLogic.getDocuments(stubs.map { it.id }).map { document ->
			stubsById.getValue(document.id).let { stub ->
				document.copy(
					delegations = document.delegations.mapValues { (s, dels) -> stub.delegations[s]?.map { delegationMapper.map(it) }?.toSet() ?: dels } +
						stub.delegations.filterKeys { k -> !document.delegations.containsKey(k) }.mapValues { (_, value) -> value.map { delegationMapper.map(it) }.toSet() },
					encryptionKeys = document.encryptionKeys.mapValues { (s, dels) -> stub.encryptionKeys[s]?.map { delegationMapper.map(it) }?.toSet() ?: dels } +
						stub.encryptionKeys.filterKeys { k -> !document.encryptionKeys.containsKey(k) }.mapValues { (_, value) -> value.map { delegationMapper.map(it) }.toSet() },
					cryptedForeignKeys = document.cryptedForeignKeys.mapValues { (s, dels) -> stub.cryptedForeignKeys[s]?.map { delegationMapper.map(it) }?.toSet() ?: dels } +
						stub.cryptedForeignKeys.filterKeys { k -> !document.cryptedForeignKeys.containsKey(k) }.mapValues { (_, value) -> value.map { delegationMapper.map(it) }.toSet() },
				)
			}.let { newDocument -> DocumentLogic.BatchUpdateDocumentInfo(newDocument, document) }
		}
		emitAll(documentLogic.createOrModifyDocuments(invoices.toList(), true).map { stubMapper.mapToStub(it) })
	}.injectReactorContext()

	@Operation(summary = "Creates or modifies a secondary attachment for a document", description = "Creates a secondary attachment for a document and returns the modified document instance afterward")
	@PutMapping("/{documentId}/secondaryAttachments/{key}", consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
	fun setSecondaryAttachment(
		@PathVariable
		documentId: String,
		@Parameter(description = "Key of the secondary attachment to update")
		@PathVariable
		key: String,
		@Parameter(description = "Revision of the latest known version of the document. If the revision does not match the current version of the document the method will fail with CONFLICT status")
		@RequestParam(required = true)
		rev: String,
		@RequestParam(required = false)
		@Parameter(description = "Utis for the attachment")
		utis: List<String>?,
		@Schema(type = "string", format = "binary")
		@RequestBody
		payload: Flow<DataBuffer>,
		@RequestHeader(name = HttpHeaders.CONTENT_LENGTH, required = false)
		lengthHeader: Long?
	): Mono<DocumentDto> = mono {
		val attachmentSize = lengthHeader ?: throw ResponseStatusException(
			HttpStatus.BAD_REQUEST,
			"Attachment size must be specified in the content-length header"
		)
		documentLogic.updateAttachmentsWrappingExceptions(
			documentLogic.getOr404(documentId).also {
				checkRevision(rev, it)
				require(key != it.mainAttachmentKey) {
					"Secondary attachments can't use $key as key: this key is reserved for the main attachment."
				}
			},
			secondaryAttachmentsChanges = mapOf(
				key to DataAttachmentChange.CreateOrUpdate(
					payload,
					attachmentSize,
					utis
				)
			)
		).let { documentMapper.map(checkNotNull(it) { "Could not update document" }) }
	}

	@Operation(summary = "Retrieve a secondary attachment of a document", description = "Get the secondary attachment with the provided key for a document")
	@GetMapping("/{documentId}/secondaryAttachments/{key}")
	fun getSecondaryAttachment(
		@PathVariable
		documentId: String,
		@Parameter(description = "Key of the secondary attachment to retrieve")
		@PathVariable
		key: String,
		@RequestParam(required = false)
		fileName: String?,
		response: ServerHttpResponse
	) = response.writeWith(
		flow {
			val document = documentLogic.getOr404(documentId).also {
				require(key != it.mainAttachmentKey) {
					"Secondary attachments can't use $key as key: this key is reserved for the main attachment."
				}
			}
			val attachment = attachmentLoader.contentFlowOfNullable(document, key) ?: throw ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"No secondary attachment with key $key for document $documentId"
			)

			response.headers["Content-Type"] = document.mainAttachment?.mimeType ?: "application/octet-stream"
			response.headers["Content-Disposition"] = "attachment; filename=\"${fileName ?: document.name}\""

			emitAll(attachment)
		}.injectReactorContext()
	)
	@Operation(summary = "Deletes a secondary attachment of a document", description = "Delete the secondary attachment with the provided key for a document")
	@DeleteMapping("/{documentId}/secondaryAttachments/{key}")
	fun deleteSecondaryAttachment(
		@PathVariable
		documentId: String,
		@Parameter(description = "Key of the secondary attachment to retrieve")
		@PathVariable
		key: String,
		@Parameter(description = "Revision of the latest known version of the document. If the revision does not match the current version of the document the method will fail with CONFLICT status")
		@RequestParam(required = true)
		rev: String,
	): Mono<DocumentDto> = mono {
		documentLogic.updateAttachments(
			documentLogic.getOr404(documentId).also {
				checkRevision(rev, it)
				require(key != it.mainAttachmentKey) {
					"Secondary attachments can't use $key as key: this key is reserved for the main attachment."
				}
			},
			secondaryAttachmentsChanges = mapOf(key to DataAttachmentChange.Delete)
		).let { documentMapper.map(checkNotNull(it) { "Could not update document" }) }
	}

	@Operation(
		summary = "Creates, modifies, or delete the attachments of a document",
		description = "Batch operation to modify multiple attachments of a document at once"
	)
	@PutMapping("/{documentId}/attachments", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
	fun setDocumentAttachments(
		@Parameter(description = "Id of the document to update")
		@PathVariable
		documentId: String,
		@Parameter(description = "Revision of the latest known version of the document. If the revision does not match the current version of the document the method will fail with CONFLICT status")
		@RequestParam(required = true)
		rev: String,
		@Parameter(description = "Describes the operations to execute with this update.")
		@RequestPart("options", required = true)
		options: BulkAttachmentUpdateOptions,
		@Parameter(description = "New attachments (to create or update). The file name will be used as the attachment key. To update the main attachment use the document id")
		@RequestPart("attachments", required = false)
		attachments: Flux<FilePart>?
	): Mono<DocumentDto> = mono {
		val attachmentsByKey: Map<String, FilePart> = attachments?.asFlow()?.toList()?.let { partsList ->
			partsList.associateBy { it.filename() }.also { partsMap ->
				require(partsList.size == partsMap.size) {
					"Duplicate keys for new attachments ${partsList.groupingBy { it.filename() }.eachCount().filter { it.value > 1 }.keys}"
				}
			}
		} ?: emptyMap()
		require(attachmentsByKey.values.all { it.headers().contentType != null }) {
			"Each attachment part must specify a ${HttpHeaders.CONTENT_TYPE} header."
		}
		require(attachmentsByKey.keys.containsAll(options.updateAttachmentsMetadata.keys)) {
			"Missing attachments for metadata: ${options.updateAttachmentsMetadata.keys - attachmentsByKey.keys}"
		}
		require(attachmentsByKey.isNotEmpty() || options.deleteAttachments.isNotEmpty()) { "Nothing to do" }
		val document = documentLogic.getOr404(documentId)
		checkRevision(rev, document)
		val mainAttachmentChange = attachmentsByKey[document.mainAttachmentKey]?.let {
			makeMultipartAttachmentUpdate("main attachment", it, options.updateAttachmentsMetadata[document.mainAttachmentKey])
		} ?: DataAttachmentChange.Delete.takeIf { document.mainAttachmentKey in options.deleteAttachments }
		val secondaryAttachmentsChanges = (options.deleteAttachments - document.mainAttachmentKey).associateWith { DataAttachmentChange.Delete } +
			(attachmentsByKey - document.mainAttachmentKey).mapValues { (key, value) ->
				makeMultipartAttachmentUpdate("secondary attachment $key", value, options.updateAttachmentsMetadata[key])
			}
		documentLogic.updateAttachmentsWrappingExceptions(document, mainAttachmentChange, secondaryAttachmentsChanges)
			.let { documentMapper.map(checkNotNull(it) { "Could not update document" }) }
	}

	private fun makeMultipartAttachmentUpdate(name: String, part: FilePart, metadata: BulkAttachmentUpdateOptions.AttachmentMetadata?) =
		DataAttachmentChange.CreateOrUpdate(
			part.content().asFlow(),
			part.headers().contentLength.takeIf { it > 0 } ?: throw ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Missing size information for $name: you must provide the size of the attachment in bytes using a Content-Length part header."
			),
			metadata?.utis
		)

	private fun checkRevision(rev: String?, document: Document) {
		if (rev != null && rev != document.rev) throw ResponseStatusException(
			HttpStatus.CONFLICT,
			"Obsolete document revision. The current revision is ${document.rev}"
		)
	}

	private suspend fun DocumentLogic.getOr404(documentId: String) =
		getDocument(documentId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No document with id $documentId")

	private suspend fun DocumentLogic.updateAttachmentsWrappingExceptions(
		currentDocument: Document,
		mainAttachmentChange: DataAttachmentChange? = null,
		secondaryAttachmentsChanges: Map<String, DataAttachmentChange> = emptyMap()
	): Document? =
		try {
			updateAttachments(currentDocument, mainAttachmentChange, secondaryAttachmentsChanges)
		} catch (e: ObjectStorageException) {
			throw ResponseStatusException(
				HttpStatus.SERVICE_UNAVAILABLE,
				"One or more attachments must be stored using the object storage service, but the service is currently unavailable."
			)
		}
}
