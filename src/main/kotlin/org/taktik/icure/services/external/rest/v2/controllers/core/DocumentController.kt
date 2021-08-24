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

package org.taktik.icure.services.external.rest.v2.controllers.core

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.taktik.commons.uti.UTI
import org.taktik.couchdb.DocIdentifier
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.DocumentLogic
import org.taktik.icure.entities.embed.Delegation
import org.taktik.icure.entities.embed.DocumentType
import org.taktik.icure.security.CryptoUtils
import org.taktik.icure.services.external.rest.v1.dto.DocumentDto
import org.taktik.icure.services.external.rest.v1.dto.IcureStubDto
import org.taktik.icure.services.external.rest.v1.dto.ListOfIdsDto
import org.taktik.icure.services.external.rest.v1.mapper.DocumentMapper
import org.taktik.icure.services.external.rest.v1.mapper.StubMapper
import org.taktik.icure.services.external.rest.v1.mapper.embed.DelegationMapper
import org.taktik.icure.utils.injectReactorContext
import reactor.core.publisher.Flux
import java.nio.ByteBuffer
import java.util.*

@ExperimentalCoroutinesApi
@RestController
@RequestMapping("/rest/v1/document")
@Tag(name = "document")
class DocumentController(private val documentLogic: DocumentLogic,
                         private val sessionLogic: AsyncSessionLogic,
                         private val documentMapper: DocumentMapper,
                         private val delegationMapper: DelegationMapper,
                         private val stubMapper: StubMapper
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "Creates a document")
    @PostMapping
    fun createDocument(@RequestBody documentDto: DocumentDto) = mono {
        val document = documentMapper.map(documentDto)
        val createdDocument = documentLogic.createDocument(document, sessionLogic.getCurrentSessionContext().getUser().healthcarePartyId!!)
                ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Document creation failed")
        documentMapper.map(createdDocument)
    }

    @Operation(summary = "Deletes a document")
    @DeleteMapping("/{documentIds}")
    fun deleteDocument(@PathVariable documentIds: String): Flux<DocIdentifier> {
        val documentIdsList = documentIds.split(',')
        return try {
            documentLogic.deleteByIds(documentIdsList).injectReactorContext()
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Document deletion failed")
        }
    }

    @Operation(summary = "Load document's attachment", responses = [ApiResponse(responseCode = "200", content = [ Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = Schema(type = "string", format = "binary"))])])
    @GetMapping("/{documentId}/attachment/{attachmentId}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getDocumentAttachment(@PathVariable documentId: String,
                      @PathVariable attachmentId: String,
                      @RequestParam(required = false) enckeys: String?,
                      @RequestParam(required = false) fileName: String?,
                      response: ServerHttpResponse) = response.writeWith(flow {
    val document = documentLogic.get(documentId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found")
        val attachment = document.decryptAttachment(if (enckeys.isNullOrBlank()) null else enckeys.split(','))
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "AttachmentDto not found")
        val uti = UTI.get(document.mainUti)
        val mimeType = if (uti != null && uti.mimeTypes.size > 0) uti.mimeTypes[0] else "application/octet-stream"

        response.headers["Content-Type"] = mimeType
        response.headers["Content-Disposition"] = "attachment; filename=\"${fileName ?: document.name}\""

        emit(DefaultDataBufferFactory().wrap(attachment))
    }.injectReactorContext())

    @Operation(summary = "Deletes a document's attachment")
    @DeleteMapping("/{documentId}/attachment")
    fun deleteAttachment(@PathVariable documentId: String) = mono {

        val document = documentLogic.get(documentId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found")

        documentLogic.modifyDocument(document.copy(attachment = null))
        documentMapper.map(document)
    }

    @Operation(summary = "Creates a document's attachment")
    @PutMapping("/{documentId}/attachment", consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun setDocumentAttachment(@PathVariable documentId: String,
                      @RequestParam(required = false) enckeys: String?,
                      @RequestBody payload: ByteArray) = mono {
        var newPayload = payload
        if (enckeys != null && enckeys.isNotEmpty()) {
            for (sfk in enckeys.split(',')) {
                val bb = ByteBuffer.wrap(ByteArray(16))
                val uuid = UUID.fromString(sfk)
                bb.putLong(uuid.mostSignificantBits)
                bb.putLong(uuid.leastSignificantBits)
                try {
                    newPayload = CryptoUtils.encryptAES(newPayload, bb.array())
                    break //should always work (no real check on key validity for encryption)
                } catch (ignored: Exception) {
                }
            }
        }

        val document = documentLogic.get(documentId)
                ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Document modification failed")
        documentLogic.modifyDocument(document.copy(attachment = newPayload))
        documentMapper.map(document)
    }

    @Operation(summary = "Creates a document's attachment")
    @PutMapping("/attachment", consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun setSafeDocumentAttachment(@RequestParam(required = true) documentId: String,
                              @RequestParam(required = false) enckeys: String?,
                              @RequestBody payload: ByteArray) = mono {
        var newPayload = payload
        if (enckeys != null && enckeys.isNotEmpty()) {
            for (sfk in enckeys.split(',')) {
                val bb = ByteBuffer.wrap(ByteArray(16))
                val uuid = UUID.fromString(sfk)
                bb.putLong(uuid.mostSignificantBits)
                bb.putLong(uuid.leastSignificantBits)
                try {
                    newPayload = CryptoUtils.encryptAES(newPayload, bb.array())
                    break //should always work (no real check on key validity for encryption)
                } catch (ignored: Exception) {
                }
            }
        }

        val document = documentLogic.get(documentId)
                ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Document modification failed")
        documentLogic.modifyDocument(document.copy(attachment = newPayload))
        documentMapper.map(document)
    }

    @Operation(summary = "Creates a document's attachment")
    @PutMapping("/{documentId}/attachment/multipart", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun setDocumentAttachmentMulti(@PathVariable documentId: String,
                           @RequestParam(required = false) enckeys: String?,
                           @RequestPart("attachment") payload: ByteArray) = setDocumentAttachment(documentId, enckeys, payload)


    @Operation(summary = "Gets a document")
    @GetMapping("/{documentId}")
    fun getDocument(@PathVariable documentId: String) = mono {
        val document = documentLogic.get(documentId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found")
        documentMapper.map(document)
    }

    @Operation(summary = "Gets a document")
    @GetMapping("/externaluuid/{externalUuid}")
    fun getDocumentByExternalUuid(@PathVariable externalUuid: String) = mono {
        val document = documentLogic.getAllByExternalUuid(externalUuid).firstOrNull()
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found")
        documentMapper.map(document)
    }

    @Operation(summary = "Get all documents with externalUuid")
    @GetMapping("/externaluuid/{externalUuid}/all")
    fun getDocumentsByExternalUuid(@PathVariable externalUuid: String) = mono {
        documentLogic.getAllByExternalUuid(externalUuid).map { documentMapper.map(it) }
    }

    @Operation(summary = "Gets a document")
    @PostMapping("/batch")
    fun getDocuments(@RequestBody documentIds: ListOfIdsDto): Flux<DocumentDto> {
        val documents = documentLogic.get(documentIds.ids)
        return documents.map { doc -> documentMapper.map(doc) }.injectReactorContext()
    }

    @Operation(summary = "Updates a document")
    @PutMapping
    fun modifyDocument(@RequestBody documentDto: DocumentDto) = mono {
        if (documentDto.id == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot modify document with no id")
        }

        val document = documentMapper.map(documentDto)
                ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Document modification failed")
        if (documentDto.attachmentId != null) {
            val prevDoc = document.id.let { documentLogic.get(it) } ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No document matching input")
            documentMapper.map(documentLogic.modifyDocument(if (documentDto.attachmentId == prevDoc.attachmentId) document.copy(
                    attachment = prevDoc.attachment,
                    attachments = prevDoc.attachments
            ) else document.copy(
                    attachments = prevDoc.attachments
            )) ?: throw IllegalStateException("Cannot update document"))
        } else
            documentMapper.map(documentLogic.modifyDocument(document) ?: throw IllegalStateException("Cannot update document") )

    }

    @Operation(summary = "Updates a batch of documents", description = "Returns the modified documents.")
    @PutMapping("/batch")
    fun modifyDocuments(@RequestBody documentDtos: List<DocumentDto>): Flux<DocumentDto> = flow{
        try {
            val indocs = documentDtos.map { f -> documentMapper.map(f) }.mapIndexed { i, doc ->
                if (doc.attachmentId != null) {
                    documentLogic.get(doc.id)?.let {
                        if (doc.attachmentId == it.attachmentId) doc.copy(
                                attachment = it.attachment,
                                attachments = it.attachments
                        ) else doc.copy(
                                attachments = it.attachments
                        )
                    } ?: doc
                } else doc
            }
            emitAll(
                    documentLogic.updateEntities(indocs)
                            .map { f -> documentMapper.map(f) }
            )
        } catch (e: Exception) {
            logger.warn(e.message, e)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

    }.injectReactorContext()

    @Operation(summary = "List documents found By Healthcare Party and secret foreign keys.", description = "Keys must be delimited by coma")
    @GetMapping("/byHcPartySecretForeignKeys")
    fun findDocumentsByHCPartyPatientForeignKeys(@RequestParam hcPartyId: String,
                                        @RequestParam secretFKeys: String): Flux<DocumentDto> {

        val secretMessageKeys = secretFKeys.split(',').map { it.trim() }
        val documentList = documentLogic.findDocumentsByHCPartySecretMessageKeys(hcPartyId, ArrayList(secretMessageKeys))
        return documentList.map { document -> documentMapper.map(document) }.injectReactorContext()
    }

    @Operation(summary = "List documents found By type, By Healthcare Party and secret foreign keys.", description = "Keys must be delimited by coma")
    @GetMapping("/byTypeHcPartySecretForeignKeys")
    fun findByTypeHCPartyMessageSecretFKeys(@RequestParam documentTypeCode: String,
                                            @RequestParam hcPartyId: String,
                                            @RequestParam secretFKeys: String): Flux<DocumentDto> {
        if (DocumentType.fromName(documentTypeCode) == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid documentTypeCode.")
        }

        val secretMessageKeys = secretFKeys.split(',').map { it.trim() }
        val documentList = documentLogic.findDocumentsByDocumentTypeHCPartySecretMessageKeys(documentTypeCode, hcPartyId, ArrayList(secretMessageKeys))

        return documentList.map { document -> documentMapper.map(document) }.injectReactorContext()
    }


    @Operation(summary = "List documents with no delegation", description = "Keys must be delimited by coma")
    @GetMapping("/woDelegation")
    fun findWithoutDelegation(@RequestParam(required = false) limit: Int?): Flux<DocumentDto> {
        val documentList = documentLogic.findWithoutDelegation(limit ?: 100)
        return documentList.map { document -> documentMapper.map(document) }.injectReactorContext()
    }

    @Operation(summary = "Update delegations in healthElements.", description = "Keys must be delimited by coma")
    @PostMapping("/delegations")
    fun setDocumentsDelegations(@RequestBody stubs: List<IcureStubDto>) = flow {
        val invoices = documentLogic.getDocuments(stubs.map { it.id }).map { document ->
            stubs.find { s -> s.id == document.id }?.let { stub ->
                document.copy(
                        delegations = document.delegations.mapValues<String, Set<Delegation>, Set<Delegation>> { (s, dels) -> stub.delegations[s]?.map { delegationMapper.map(it) }?.toSet() ?: dels },
                        encryptionKeys = document.encryptionKeys.mapValues<String, Set<Delegation>, Set<Delegation>> { (s, dels) -> stub.encryptionKeys[s]?.map { delegationMapper.map(it) }?.toSet() ?: dels },
                        cryptedForeignKeys = document.cryptedForeignKeys.mapValues<String, Set<Delegation>, Set<Delegation>> { (s, dels) -> stub.cryptedForeignKeys[s]?.map { delegationMapper.map(it) }?.toSet() ?: dels }
                )
            } ?: document
        }
        emitAll(documentLogic.updateDocuments(invoices.toList()).map { stubMapper.mapToStub(it) })
    }.injectReactorContext()
}
