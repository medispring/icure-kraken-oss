package org.taktik.icure.services.external.rest.v1.dto.embed.form.template

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.taktik.icure.entities.FormTemplate
import org.taktik.icure.services.external.rest.v1.mapper.FormTemplateMapperImpl
import org.taktik.icure.services.external.rest.v1.mapper.base.CodeStubMapperImpl
import org.taktik.icure.services.external.rest.v1.mapper.embed.DocumentGroupMapperImpl

internal class FormTemplateLayoutTest {
	@Test
	fun map() {
		val objectMapper = ObjectMapper().registerModule(
			KotlinModule.Builder()
				.nullIsSameAsDefault(nullIsSameAsDefault = false)
				.reflectionCacheSize(reflectionCacheSize = 512)
				.nullToEmptyMap(nullToEmptyMap = false)
				.nullToEmptyCollection(nullToEmptyCollection = false)
				.singletonSupport(singletonSupport = SingletonSupport.DISABLED)
				.strictNullChecks(strictNullChecks = false)
				.build()
		)
		val layout = objectMapper.writeValueAsBytes(
			FormTemplateLayout(
				form = "My form",
				sections = listOf(
					Section(
						section = "First section",
						fields = listOf(
							TextField("text"),
							NumberField("number"),
							DatePicker("date")
						)
					)
				)
			)
		)
		val result = FormTemplateMapperImpl(DocumentGroupMapperImpl(), CodeStubMapperImpl()).map(
			FormTemplate(
				id = "123",
				templateLayout = layout
			)
		)
		assertNotNull(result)
		assertNotNull(result.templateLayout)
		assertEquals(result.templateLayout?.sections?.firstOrNull()?.fields?.size, 3)

		val reverse = objectMapper.readValue<FormTemplateLayout>(layout)
		assertEquals(reverse.sections.firstOrNull()?.fields?.size, 3)
	}

	@Test
	fun readComplexExample() {
		val layout = """form: Entretien préliminaire Psycho Social
description: Entretien préliminaire Psycho Social
sections:
  - section:
    fields:
      - field: Type de consultation
        type: dropdown
        labels:
          above : Type de consultation
        shortLabel: contactType
        options:
          home: Sur place
          visio: Visioconférence
          call: Téléphone
        rows: 1
        columns: 1
        required: true
        codifications :
          - MS-IVG-CONTACT-TYPE
        tags:
          - MS-IVG|CONTACT-TYPE|1
      - field : waitingRoomFollowersNumber
        type: number-field
        shortLabel: waitingRoomFollowersNumber
        value: 0
        labels:
          above: Accompagné de
          right: personne(s) (salle d'attente)
        rows: 2
        columns: 1
        required: true
        tags :
          - CD-CUSTOM-IVG|WAITING-ROOM-FOLLOWERS-NUMBER|1
      - field : consultationFollowersNumber
        type: number-field
        shortLabel: consultationFollowersNumber
        value: 0
        labels:
          above: Accompagné de
          right: personne(s) (entretien préliminaire)
        rows: 2
        columns: 1
        required: true
        tags :
          - CD-CUSTOM-IVG|CONSULTATION-FOLLOWERS-NUMBER|1
      - field : Personnes Accompagnants
        type: checkbox
        shortLabel: PersonFollowerType
        options:
          option1: Partenaire
          option2: Mère
          option3: Amie
          option4: Éducateur
          option5: Père
        rows: 3
        columns: 1
        required: false
        tags:
          - CD-CUSTOM-IVG|PERSON-FOLLOWER-TYPE|1
      - field : Profession de la patiente
        type: radio-button
        shortLabel: PatientProfession
        options:
          option1: Employé
          option2: Ouvrier
          option3: Indépendant
          option4: Sans travail
          option5: Ne veut pas dire
        rows: 4
        columns: 1
        required: false
        tags:
          - CD-CUSTOM-IVG|PATIENT-PROFESSION|1
      - field : Nombre d’enfants
        type: number-field
        shortLabel: ChildNumber
        value: 0
        rows: 5
        columns: 1
        required: true
        tags:
          - CD-CUSTOM-IVG|CHILD-NUMBER|1
      - field : Nombre d’enfants à charge
        type: number-field
        shortLabel: ChildInChargeNumber
        value: 0
        rows: 5
        columns: 1
        required: true
        tags:
          - CD-CUSTOM-IVG|CHILD-IN-CHARGE-NUMBER|1
      - field : Nombre de minutes de suivi
        type: measure-field
        shortLabel: minutesTrackingNumber
        rows: 6
        columns: 1
        value: 50
        unit: min
        required: true
        tags:
          - CD-CUSTOM-IVG|MINUTES-TRACKING-NUMBER|1
      - field : Nombre de minutes de contraception
        type: measure-field
        shortLabel: minutesContraceptionNumber
        rows: 6
        columns: 1
        required: true
        value: 10
        unit: min
        tags:
          - CD-CUSTOM-IVG|MINUTES-CONTRACEPTION-NUMBER|1
      - field : Historique/Situation
        type: textfield
        shortLabel: History
        rows: 7
        columns: 1
        required: false
        tags:
          - CD-CUSTOM-IVG|HISTORY|1
      - field : Position du partenaire pendant la grossesse
        type: textfield
        shortLabel: SexualPosition
        rows: 8
        columns: 1
        required: false
        tags:
          - CD-CUSTOM-IVG|SEXUAL-POSITION|1
      - field : Processus de prise de décision / choix / attitude par rapport à l'avortement (le cas échéant)
        type: textfield
        shortLabel: decisionProcessus
        rows: 9
        columns: 1
        required: false
        tags :
          -  CD-CUSTOM-IVG|DECISION-PROCESSUS|1
      - field : Première décision de la patiente
        type: radio-button
        shortLabel: firstChoice
        rows: 9
        columns: 1
        required: true
        options:
          option1: Oui par curretage
          option2: Ne sait pas
          option3: Oui par médication
          option4: Non
        tags:
          - CD-CUSTOM-IVG|FIRST-CHOICE|1
      - field : Notes
        type: textfield
        shortLabel: notes
        rows: 10
        columns: 1
        required: false
        multiline: true
        tags:
          - CD-CUSTOM-IVG|NOTES|1
"""

		val yaml: ObjectMapper = ObjectMapper(YAMLFactory()).registerModule(
			KotlinModule.Builder()
				.nullIsSameAsDefault(true)
				.nullToEmptyCollection(true)
				.nullToEmptyMap(true)
				.build()
		).apply { setSerializationInclusion(JsonInclude.Include.NON_NULL) }

		try {
			val result = yaml.readValue<FormTemplateLayout>(layout)
		} catch (e: Exception) {
			e.printStackTrace()
			throw e
		}
	}
}
