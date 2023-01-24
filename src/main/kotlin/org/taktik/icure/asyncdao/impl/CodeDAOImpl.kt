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

package org.taktik.icure.asyncdao.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.transform
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import org.taktik.couchdb.Offset
import org.taktik.couchdb.TotalCount
import org.taktik.couchdb.ViewQueryResultEvent
import org.taktik.couchdb.ViewRowWithDoc
import org.taktik.couchdb.annotation.View
import org.taktik.couchdb.entity.ComplexKey
import org.taktik.couchdb.id.IDGenerator
import org.taktik.couchdb.queryView
import org.taktik.couchdb.queryViewIncludeDocsNoValue
import org.taktik.icure.asyncdao.CodeDAO
import org.taktik.icure.db.PaginationOffset
import org.taktik.icure.db.StringUtils
import org.taktik.icure.entities.base.Code
import org.taktik.icure.properties.CouchDbProperties
import org.taktik.icure.spring.asynccache.AsyncCacheManager

data class QueryResultAccumulator(
	val seenElements: Int = 0,
	val sentElements: Int = 0,
	val elementsFound: Int? = null,
	val toEmit: ViewQueryResultEvent? = null,
	val lastVisited: ViewRowWithDoc<*, *, *>? = null,
	val offset: Int? = null
)

data class ResultsWithAccumulator(
	val results: Map<String, ViewRowWithDoc<*,*,*>> = mapOf(),
	val accumulator: QueryResultAccumulator = QueryResultAccumulator()
)

data class CodeAccumulator(
	val code: Code? = null,
	val toEmit: Code? = null
)

@Repository("codeDAO")
@View(name = "all", map = "function(doc) { if (doc.java_type == 'org.taktik.icure.entities.base.Code' && !doc.deleted) emit( null, doc._id )}")
class CodeDAOImpl(
	couchDbProperties: CouchDbProperties,
	@Qualifier("baseCouchDbDispatcher") couchDbDispatcher: CouchDbDispatcher,
	idGenerator: IDGenerator,
	@Qualifier("asyncCacheManager") asyncCacheManager: AsyncCacheManager
) : CachedDAOImpl<Code>(Code::class.java, couchDbProperties, couchDbDispatcher, idGenerator, asyncCacheManager), CodeDAO {

	companion object {
		private const val SMALLEST_CHAR = "\u0000"
	}

	@View(name = "by_type_code_version", map = "classpath:js/code/By_type_code_version.js", reduce = "_count")
	override fun listCodesBy(type: String?, code: String?, version: String?): Flow<Code> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		emitAll(
			client.queryViewIncludeDocsNoValue<Array<String>, Code>(
				createQuery(client, "by_type_code_version")
					.includeDocs(true)
					.reduce(false)
					.startKey(
						ComplexKey.of(
							type,
							code,
							version
						)
					)
					.endKey(
						ComplexKey.of(
							type ?: ComplexKey.emptyObject(),
							code ?: ComplexKey.emptyObject(),
							version ?: ComplexKey.emptyObject()
						)
					)
			).map { it.doc }

		)
	}

	override fun listCodesByType(type: String?): Flow<String> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		emitAll(
			client.queryView<String, String>(
				createQuery(client, "by_type_code_version")
					.includeDocs(false)
					.group(true)
					.groupLevel(2)
					.startKey(ComplexKey.of(type, null, null))
					.endKey(ComplexKey.of(if (type == null) null else type + "\ufff0", null, null))
			).mapNotNull { it.key }
		)
	}

	@View(name = "by_region_type_code_version", map = "classpath:js/code/By_region_type_code_version.js", reduce = "_count")
	override fun listCodesBy(region: String?, type: String?, code: String?, version: String?): Flow<Code> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		var lastCode: Code? = null
		emitAll(
			client.queryViewIncludeDocsNoValue<Array<String>, Code>(
				createQuery(client, "by_region_type_code_version")
					.includeDocs(true)
					.reduce(false)
					.startKey(
						ComplexKey.of(
							region ?: SMALLEST_CHAR,
							type ?: SMALLEST_CHAR,
							code ?: SMALLEST_CHAR,
							if (version == null || version == "latest") SMALLEST_CHAR else version
						)
					)
					.endKey(
						ComplexKey.of(
							region ?: ComplexKey.emptyObject(),
							type ?: ComplexKey.emptyObject(),
							code ?: ComplexKey.emptyObject(),
							version ?: ComplexKey.emptyObject()
						)
					)
			).map {
				it.doc
			}.let { flw ->
				if (version == "latest") {
					flw.scan(CodeAccumulator()) { acc, code ->
						lastCode = code
						acc.code?.let {
							if (code.type != it.type || code.code != it.code) CodeAccumulator(code, it)
							else CodeAccumulator(code)
						} ?: CodeAccumulator(code)
					}.mapNotNull{
						it.toEmit
					}
				} else flw
			}.onCompletion {
				if (lastCode != null) emit(lastCode!!)
			}
		)
	}

	override fun listCodesByRegionAndType(region: String?, type: String?): Flow<String> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		emitAll(
			client.queryView<List<String>, String>(
				createQuery(client, "by_region_type_code_version")
					.includeDocs(false)
					.group(true)
					.groupLevel(2)
					.startKey(ComplexKey.of(region, type ?: "", null, null))
					.endKey(ComplexKey.of(region, if (type == null) ComplexKey.emptyObject() else type + "\ufff0", null, null))
			).mapNotNull { it.key?.get(1) }
		)
	}

	fun accumulateLatestVersionOrNull(acc: QueryResultAccumulator, row: ViewRowWithDoc<*, *, *>, limit: Int,): QueryResultAccumulator {
		return if (acc.lastVisited != null && // If I have something to emit
			acc.sentElements < limit && // And I still have space on the page
			(
				(acc.lastVisited.doc as Code).code != (row.doc as Code).code || // The codes are sorted, If this one is different for something
					(acc.lastVisited.doc as Code).type != (row.doc as Code).type
				)
		)
			QueryResultAccumulator(acc.seenElements + 1, acc.sentElements + 1, acc.elementsFound, acc.lastVisited, row, acc.offset)
		else QueryResultAccumulator(acc.seenElements + 1, acc.sentElements, acc.elementsFound, null, row, acc.offset)
	}

	fun accumulateVersionOrNull(acc: QueryResultAccumulator, row: ViewRowWithDoc<*, *, *>, version: String, skip: Boolean): QueryResultAccumulator {
		return if ((acc.lastVisited != null || !skip) && // If it is the second or later call, I have to skip the first result (otherwise is repeated)
			(row.doc as Code).version == version // And the version is correct
		)
			QueryResultAccumulator(acc.seenElements + 1, acc.sentElements + 1, acc.elementsFound, row, row)
		else QueryResultAccumulator(acc.seenElements + 1, acc.sentElements, acc.elementsFound,null, row)
	}

	@ExperimentalCoroutinesApi
	@FlowPreview
	override fun findCodesBy(region: String?, type: String?, code: String?, version: String?, paginationOffset: PaginationOffset<List<String?>>, extensionFactor: Float?): Flow<ViewQueryResultEvent> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)

		val from = ComplexKey.of(
			region ?: SMALLEST_CHAR,
			type ?: SMALLEST_CHAR,
			code ?: SMALLEST_CHAR,
			if (version == null || version == "latest") SMALLEST_CHAR else version
		)
		val to = ComplexKey.of(
			region ?: ComplexKey.emptyObject(),
			type ?: ComplexKey.emptyObject(),
			if (code == null) ComplexKey.emptyObject() else if (version == null) code + "\ufff0" else code,
			if (version == null || version == "latest") ComplexKey.emptyObject() else version + "\ufff0"
		)

		val extendedLimit = (paginationOffset.limit * ( extensionFactor ?: 1f) ).toInt()
		val viewQuery = pagedViewQuery<Code, ComplexKey>(
			client,
			"by_region_type_code_version",
			from,
			to,
			paginationOffset.toPaginationOffset { ComplexKey.of(*it.toTypedArray())}.copy(limit = extendedLimit),
			false
		)
		var versionAccumulator = QueryResultAccumulator()
		emitAll(
			client.queryView(viewQuery, Array<String>::class.java, String::class.java, Code::class.java).let { flw ->
				if (version == null || version != "latest") flw
				else flw.scan(QueryResultAccumulator()) { acc, it ->
					when (it) {
						is ViewRowWithDoc<*, *, *> -> accumulateLatestVersionOrNull(acc, it, paginationOffset.limit)
						is TotalCount -> QueryResultAccumulator(acc.seenElements, acc.sentElements, it.total, null, acc.lastVisited, acc.offset)
						is Offset -> QueryResultAccumulator(acc.seenElements, acc.sentElements, acc.elementsFound, null, acc.lastVisited, it.offset)
						else -> QueryResultAccumulator(acc.seenElements, acc.sentElements, acc.elementsFound,null, acc.lastVisited, acc.offset)
					}
				}.transform {
					if (it.toEmit != null) emit(it.toEmit) // If I have something to emit, I emit it
					versionAccumulator = it
				}.onCompletion {
					// If it viewed all the elements there can be more
					// AND it did not fill the page
					// it does the recursive call
					if (versionAccumulator.seenElements >= extendedLimit && versionAccumulator.sentElements < paginationOffset.limit)
						emitAll(
							findCodesBy(
								region,
								type,
								code,
								version,
								paginationOffset.copy(startKey = (versionAccumulator.lastVisited?.key as? Array<String>)?.toList(), startDocumentId = versionAccumulator.lastVisited?.id, limit = paginationOffset.limit - versionAccumulator.sentElements),
								(if (versionAccumulator.seenElements == 0) ( extensionFactor ?: 1f) * 2 else (versionAccumulator.seenElements.toFloat() / versionAccumulator.sentElements)).coerceAtMost(100f)
							)
						)
					else {
						// If the version filter is latest and there are no more elements to visit and the page is not full, I emit the last element
						if (versionAccumulator.lastVisited != null && versionAccumulator.sentElements < paginationOffset.limit)
							emit(versionAccumulator.lastVisited as ViewQueryResultEvent) //If the version filter is "latest" then the last code must be always emitted
						emit(TotalCount(versionAccumulator.elementsFound ?: 0))
					}
				}
			}
		)
	}

	// Recursive function to filter results by version
	// If the filtered results are not enough to fill a page, it does the recusive step
	fun findCodesByLabel(from: ComplexKey, to: ComplexKey, version: String?, viewName: String, mapIndex: Int, paginationOffset: PaginationOffset<List<String?>>, extensionFactor: Float, prevTotalCount: Int, isContinue: Boolean): Flow<ViewQueryResultEvent> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		val extendedLimit = (paginationOffset.limit * extensionFactor).toInt()
		val viewQuery = pagedViewQuery<Code, ComplexKey>(
			client,
			viewName,
			from,
			to,
			paginationOffset.copy(limit = extendedLimit).toPaginationOffset { sk -> ComplexKey.of(*sk.mapIndexed { i, s -> if (i == mapIndex) s?.let { StringUtils.sanitizeString(it) } else s }.toTypedArray()) },
			false
		)

		var versionAccumulator = QueryResultAccumulator()
		emitAll(
			client.queryView(viewQuery, Array<String>::class.java, String::class.java, Code::class.java).let { flw ->
				if (version == null) flw//If I have no version filter, I just return the flow
				else {
					val flowToEmit = if (version != "latest")
						flw.scan(QueryResultAccumulator()) { acc, it ->
							when (it) {
								is ViewRowWithDoc<*, *, *> ->
									if (acc.sentElements < paginationOffset.limit)
										accumulateVersionOrNull(acc, it, version, isContinue)
									else QueryResultAccumulator(acc.seenElements+1, acc.sentElements, acc.elementsFound, null, acc.lastVisited)
								is TotalCount -> QueryResultAccumulator(acc.seenElements, acc.sentElements, it.total, null, acc.lastVisited)
								else -> QueryResultAccumulator(acc.seenElements, acc.sentElements, acc.elementsFound, null, acc.lastVisited)
							}
						} else {
						// Here I have to get all the latest version of the codes

						//First, I iterate over the flow results and I keep only the latest version of each code
						//NOTE: Codes are ordered lexicographically
						val codeMap = flw.fold(ResultsWithAccumulator()) { acc, it ->
							if (it is ViewRowWithDoc<*, *, *>) {
								val currentCode = it.doc as Code
								if (currentCode.type != null && currentCode.code != null)
									acc.results["${currentCode.type}|${currentCode.code}"]?.let { oldRow ->
										if ((oldRow.doc as Code).id.lowercase() >= currentCode.id.lowercase()) ResultsWithAccumulator(acc.results, QueryResultAccumulator(acc.accumulator.seenElements + 1))
										else ResultsWithAccumulator(acc.results + ("${currentCode.type}|${currentCode.code}" to it), QueryResultAccumulator(acc.accumulator.seenElements + 1))
									} ?: ResultsWithAccumulator(acc.results + ("${currentCode.type}|${currentCode.code}" to it), QueryResultAccumulator(acc.accumulator.seenElements + 1))
								else acc
							} else acc
						}
						val orderedKeys = codeMap.results.keys.sorted()
						val (startType, startCode) = orderedKeys.first().split('|')
						val (endType, endCode) = orderedKeys.last().split('|')
						// Then, I check If a more recent version of the code exists.
						// If it wasn't in the previous result, it means that the new version does not match the label
						val latestVersions = listCodeIdsByTypeCodeVersionInterval(
							startType = startType,
							startCode = startCode,
							startVersion = null,
							endType = endType,
							endCode = endCode,
							endVersion = null
						).fold(mapOf<String, String>()) { acc, it ->
							val (type, code, _) = it.split('|')
							if (codeMap.results.containsKey("${type}|${code}")) {
								acc["${type}|${code}"]?.let { oldCode ->
									if (oldCode.lowercase() >= it.lowercase()) acc
									else acc + ("${type}|${code}" to it)
								} ?: (acc + ("${type}|${code}" to it))
							} else acc
						}
						//Finally, I filter and emit the results
						codeMap.results.keys.asFlow().scan(codeMap.accumulator) { acc, it ->
							if (acc.sentElements < paginationOffset.limit) {
								latestVersions[it]?.let { oldCode ->
									val newCode = codeMap.results[it]?.doc as Code
									//If this condition is true, it means that the code I found it is at its latest version AND the label matches the filter
									// SO I want to emit it
									if ( newCode.id == oldCode ) {
										QueryResultAccumulator(acc.seenElements, acc.sentElements + 1, acc.elementsFound, codeMap.results[it]!!, codeMap.results[it])
									} else acc.copy(toEmit = null)
									// If the condition is false, I can be in one of 2 cases
									// 1) The latest version of the code is in another page, so I want to emit it later
									// 2) The label of the latest version of the code does not match the filter, so I do not want to emit it
								}?: acc.copy(toEmit = null)
							} else acc.copy(toEmit = null)
						}

					}

					flowToEmit.transform {
						if (it.toEmit != null) emit(it.toEmit) //If I have something to emit, I emit it
						versionAccumulator = it
					}.onCompletion {
						// If it viewed all the elements there can be more
						// AND it did not fill the page
						// it does the recursive call
						if (versionAccumulator.seenElements >= extendedLimit && versionAccumulator.sentElements < paginationOffset.limit)
							emitAll(
								findCodesByLabel(
									from,
									to,
									version,
									viewName,
									mapIndex,
									paginationOffset.copy(startKey = (versionAccumulator.lastVisited?.key as? Array<String>)?.toList(), startDocumentId = versionAccumulator.lastVisited?.id, limit = paginationOffset.limit - versionAccumulator.sentElements),
									(if (versionAccumulator.seenElements == 0) extensionFactor * 2 else (versionAccumulator.seenElements.toFloat() / versionAccumulator.sentElements)).coerceAtMost(100f),
									versionAccumulator.sentElements + prevTotalCount,
									true
								)
							)
						else emit(TotalCount(versionAccumulator.elementsFound ?: 0))
					}
				}
			}
		)
	}

	@ExperimentalCoroutinesApi
	@FlowPreview
	@View(name = "by_language_label", map = "classpath:js/code/By_language_label.js")
	override fun findCodesByLabel(region: String?, language: String?, label: String?, version: String?, paginationOffset: PaginationOffset<List<String?>>): Flow<ViewQueryResultEvent> {
		val sanitizedLabel = label?.let { StringUtils.sanitizeString(it) }
		val from = ComplexKey.of(
			region ?: SMALLEST_CHAR,
			language ?: SMALLEST_CHAR,
			sanitizedLabel ?: SMALLEST_CHAR
		)

		val to = ComplexKey.of(
			if (region == null) ComplexKey.emptyObject() else if (language == null) region + "\ufff0" else region,
			if (language == null) ComplexKey.emptyObject() else if (sanitizedLabel == null) language + "\ufff0" else language,
			if (sanitizedLabel == null) ComplexKey.emptyObject() else sanitizedLabel + "\ufff0"
		)
		return findCodesByLabel(from, to, version, "by_language_label", 2, paginationOffset, 1f, 0, false)
	}

	@View(name = "by_language_type_label", map = "classpath:js/code/By_language_type_label.js")
	override fun findCodesByLabel(region: String?, language: String?, type: String?, label: String?, version: String?, paginationOffset: PaginationOffset<List<String?>>): Flow<ViewQueryResultEvent> {
		val sanitizedLabel = label?.let { StringUtils.sanitizeString(it) }
		val from = ComplexKey.of(
			region ?: SMALLEST_CHAR,
			language ?: SMALLEST_CHAR,
			type ?: SMALLEST_CHAR,
			sanitizedLabel ?: SMALLEST_CHAR
		)

		val to = ComplexKey.of(
			if (region == null) ComplexKey.emptyObject() else if (language == null) region + "\ufff0" else region,
			language ?: ComplexKey.emptyObject(),
			type ?: ComplexKey.emptyObject(),
			if (sanitizedLabel == null) ComplexKey.emptyObject() else sanitizedLabel + "\ufff0"
		)

		return findCodesByLabel(from, to, version, "by_language_type_label", 3, paginationOffset, 1f, 0, false)
	}

	@View(name = "conflicts", map = "function(doc) { if (doc.java_type == 'org.taktik.icure.entities.base.Code' && !doc.deleted && doc._conflicts) emit(doc._id )}")
	override fun listConflicts(): Flow<Code> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)

		emitAll(client.queryViewIncludeDocsNoValue<String, Code>(createQuery(client, "conflicts").includeDocs(true)).map { it.doc })
	}

	@View(name = "by_qualifiedlink_id", map = "classpath:js/code/By_qualifiedlink_id.js")
	override fun findCodesByQualifiedLinkId(region: String?, linkType: String, linkedId: String?, paginationOffset: PaginationOffset<List<String>>): Flow<ViewQueryResultEvent> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		val from =
			ComplexKey.of(
				linkType,
				linkedId
			)
		val to = ComplexKey.of(
			linkType,
			linkedId ?: ComplexKey.emptyObject()
		)

		val viewQuery = pagedViewQuery<Code, ComplexKey>(
			client,
			"by_qualifiedlink_id",
			from,
			to,
			paginationOffset.toPaginationOffset { ComplexKey.of(*it.toTypedArray()) },
			false
		)
		emitAll(client.queryView(viewQuery, Array<String>::class.java, String::class.java, Code::class.java))
	}

	override fun listCodeIdsByLabel(region: String?, language: String?, label: String?): Flow<String> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		val sanitizedLabel = label?.let { StringUtils.sanitizeString(it) }
		val from =
			ComplexKey.of(
				region ?: SMALLEST_CHAR,
				language ?: SMALLEST_CHAR,
				sanitizedLabel ?: SMALLEST_CHAR
			)

		val to = ComplexKey.of(
			if (region == null) ComplexKey.emptyObject() else if (language == null) region + "\ufff0" else region,
			if (language == null) ComplexKey.emptyObject() else if (sanitizedLabel == null) language + "\ufff0" else language,
			if (sanitizedLabel == null) ComplexKey.emptyObject() else sanitizedLabel + "\ufff0"
		)

		emitAll(
			client.queryView<String, String>(
				createQuery(client, "by_language_label")
					.includeDocs(false)
					.startKey(from)
					.endKey(to)
			).mapNotNull { it.key }
		)
	}

	override fun listCodeIdsByLabel(region: String?, language: String?, type: String?, label: String?): Flow<String> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		val sanitizedLabel = label?.let { StringUtils.sanitizeString(it) }
		val from =
			ComplexKey.of(
				region ?: SMALLEST_CHAR,
				language ?: SMALLEST_CHAR,
				type ?: SMALLEST_CHAR,
				sanitizedLabel ?: SMALLEST_CHAR
			)
		val to = ComplexKey.of(
			if (region == null) ComplexKey.emptyObject() else if (language == null) region + "\ufff0" else region,
			language ?: ComplexKey.emptyObject(),
			type ?: ComplexKey.emptyObject(),
			if (sanitizedLabel == null) ComplexKey.emptyObject() else sanitizedLabel + "\ufff0"
		)

		emitAll(
			client.queryView<Array<String>, Int>(
				createQuery(client, "by_language_type_label")
					.includeDocs(false)
					.startKey(from)
					.endKey(to)
			).mapNotNull { it.id }
		)
	}

	override fun listCodeIdsByTypeCodeVersionInterval(startType: String?, startCode: String?, startVersion: String?, endType: String?, endCode: String?, endVersion: String?): Flow<String> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		val from = ComplexKey.of(
			startType ?: SMALLEST_CHAR,
			startCode ?: SMALLEST_CHAR,
			startVersion ?: SMALLEST_CHAR,
		)
		val to = ComplexKey.of(
			endType ?: ComplexKey.emptyObject(),
			endCode ?: ComplexKey.emptyObject(),
			endVersion ?: ComplexKey.emptyObject(),
		)
		emitAll(
			client.queryView<Array<String>, String>(
				createQuery(client, "by_type_code_version")
					.includeDocs(false)
					.reduce(false)
					.startKey(from)
					.endKey(to)
			).mapNotNull { it.id }
		)
	}

	override fun listCodeIdsByQualifiedLinkId(linkType: String, linkedId: String?): Flow<String> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		val from = ComplexKey.of(
			linkType,
			linkedId
		)
		val to = ComplexKey.of(
			linkType,
			linkedId ?: ComplexKey.emptyObject()
		)

		emitAll(
			client.queryView<String, String>(
				createQuery(client, "by_qualifiedlink_id")
					.includeDocs(false)
					.startKey(from)
					.endKey(to)
			).mapNotNull { it.id }
		)
	}

	override fun getCodesByIdsForPagination(ids: List<String>): Flow<ViewQueryResultEvent> = flow {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		emitAll(client.getForPagination(ids, Code::class.java))
	}

	override suspend fun isValid(codeType: String, codeCode: String, codeVersion: String?) = listCodesBy(codeType, codeCode, codeVersion).firstOrNull() != null

	override suspend fun getCodeByLabel(region: String, label: String, ofType: String, labelLang: List<String>): Code? {
		val client = couchDbDispatcher.getClient(dbInstanceUrl)
		val sanitizedLabel = label.let { StringUtils.sanitizeString(it) }
		for (lang in labelLang) {
			val codeFlow = client.queryViewIncludeDocsNoValue<Array<String>, Code>(
				createQuery(client, "by_region_type_code_version")
					.includeDocs(true)
					.reduce(false)
					.key(
						ComplexKey.of(
							region,
							lang,
							ofType,
							sanitizedLabel
						)
					)
			).map { it.doc }.filter { c -> c.label?.get(lang)?.let { StringUtils.sanitizeString(it) } == sanitizedLabel }
			val code = codeFlow.firstOrNull()
			if (code != null) {
				return code
			}
		}

		//throw IllegalArgumentException("code of type $ofType not found for label $label in languages $labelLang")
		return null
	}
}
