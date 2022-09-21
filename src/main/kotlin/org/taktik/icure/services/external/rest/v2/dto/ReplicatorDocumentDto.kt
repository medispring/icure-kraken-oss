/*
 *    Copyright 2020 Taktik SA
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package org.taktik.icure.services.external.rest.v2.dto

import java.io.Serializable
import java.time.ZonedDateTime
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.pozo.KotlinBuilder
import org.taktik.couchdb.handlers.ZonedDateTimeDeserializer
import org.taktik.couchdb.handlers.ZonedDateTimeSerializer
import org.taktik.icure.services.external.rest.v2.dto.base.VersionableDto

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class ReplicatorDocumentDto(
        override val id: String,
        override val rev: String?,
        val source: ReplicateCommandDto.RemoteDto? = null,
        val target: ReplicateCommandDto.RemoteDto? = null,
        val owner: String? = null,
        val create_target: Boolean? = null,
        val continuous: Boolean? = null,
        val doc_ids: List<String>? = null,
        val replicationState: String? = null,
        @JsonSerialize(using = ZonedDateTimeSerializer::class)
        @JsonDeserialize(using = ZonedDateTimeDeserializer::class)
        val replicationStateTime: ZonedDateTime? = null,
        val replicationStats: ReplicationStatsDto? = null,
        val errorCount: Int? = null,
) : VersionableDto<String> {
    override fun withIdRev(id: String?, rev: String) = id?.let { this.copy(id = it, rev = rev) } ?: this.copy(rev = rev)
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class ReplicationStatsDto(
        val revisionsChecked: Int? = null,
        val missingRevisionsFound: Int? = null,
        val docsRead: Int? = null,
        val docsWritten: Int? = null,
        val changesPending: Int? = null,
        val docWriteFailures: Int? = null,
        val checkpointedSourceSeq: String? = null,
        @JsonSerialize(using = ZonedDateTimeSerializer::class)
        @JsonDeserialize(using = ZonedDateTimeDeserializer::class)
        val startTime: ZonedDateTime? = null,
        val error: String? = null
) : Serializable
