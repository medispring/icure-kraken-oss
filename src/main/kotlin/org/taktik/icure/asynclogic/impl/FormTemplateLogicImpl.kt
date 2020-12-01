/*
 * Copyright (C) 2018 Taktik SA
 *
 * This file is part of iCureBackend.
 *
 * iCureBackend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * iCureBackend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with iCureBackend.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taktik.icure.asynclogic.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Charsets
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.taktik.icure.asyncdao.FormTemplateDAO
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.FormTemplateLogic
import org.taktik.icure.dto.gui.layout.FormLayout

import org.taktik.icure.entities.FormTemplate
import org.taktik.icure.utils.firstOrNull
import java.util.*

@ExperimentalCoroutinesApi
@Service
class FormTemplateLogicImpl(private val formTemplateDAO: FormTemplateDAO,
                            private val sessionLogic: AsyncSessionLogic,
                            private val objectMapper: ObjectMapper) : GenericLogicImpl<FormTemplate, FormTemplateDAO>(sessionLogic), FormTemplateLogic {

    override fun createEntities(entities: Collection<FormTemplate>, createdEntities: Collection<FormTemplate>) = flow {
        emitAll(super.createEntities(entities))
    }

    override suspend fun createFormTemplate(entity: FormTemplate) = fix(entity) { entity ->
        formTemplateDAO.createFormTemplate(entity)
    }

    override suspend fun getFormTemplateById(formTemplateId: String): FormTemplate? {
        return formTemplateDAO.get(formTemplateId)
    }

    override fun getFormTemplatesByGuid(userId: String, specialityCode: String, formTemplateGuid: String): Flow<FormTemplate> = flow {
        val byUserGuid = formTemplateDAO.findByUserGuid(userId, formTemplateGuid, true)
        if (byUserGuid.firstOrNull() != null) {
            emitAll(byUserGuid)
        } else {
            emitAll(formTemplateDAO.findBySpecialtyGuid(specialityCode, formTemplateGuid, true))
        }
    }

    override fun getFormTemplatesBySpecialty(specialityCode: String, loadLayout: Boolean): Flow<FormTemplate> = flow {
        emitAll(formTemplateDAO.findBySpecialtyGuid(specialityCode, null, loadLayout))
    }

    override fun getFormTemplatesByUser(userId: String, loadLayout: Boolean): Flow<FormTemplate> = flow {
        emitAll(formTemplateDAO.findByUserGuid(userId, null, loadLayout))
    }

    override suspend fun modifyFormTemplate(formTemplate: FormTemplate) = fix(formTemplate) { formTemplate ->
        formTemplateDAO.save(formTemplate)
    }

    override suspend fun build(data: ByteArray): FormLayout {
        return objectMapper.readValue(String(data, Charsets.UTF_8), FormLayout::class.java)
    }

    override fun getGenericDAO(): FormTemplateDAO {
        return formTemplateDAO
    }

    override fun getFieldsNames(formLayout: FormLayout): List<String> {
        val fieldNames: MutableList<String> = ArrayList()
        val sections = formLayout.sections
        sections.forEach { section ->
            val formColumns = section.formColumns
            formColumns.forEach { column ->
                val formDataList = column.formDataList
                formDataList.forEach { formData -> fieldNames.add(formData.name) }
            }
        }
        return fieldNames
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FormTemplateLogicImpl::class.java)
    }
}
