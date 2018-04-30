/**
 * 
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 2.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {XHR} from "./XHR"
import * as models from '../model/models';

export class iccBehubsApi {
    host : string
    constructor(host: string) {
        this.host = host
    }


    handleError(e: XHR.Data) {
        if (e.status == 401) throw Error('auth-failed')
        else throw Error('api-error'+ e.status)
    }


    checkHcPartyConsent(token: string, inss?: string, nihii?: string) : Promise<models.HcPartyConsent|any> {
        let _body = null
        
        
        const _url = this.host+"/be_hubs/hcpconsent/{token}".replace("{token}", token+"") + "?ts=" + (new Date).getTime()  + (inss ? "&inss=" + inss : "") + (nihii ? "&nihii=" + nihii : "")

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc =>  new models.HcPartyConsent(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    getPatientConsent(token: string, ssinPatient: string) : Promise<models.Consent|any> {
        let _body = null
        
        
        const _url = this.host+"/be_hubs/consent/{token}/{ssinPatient}".replace("{token}", token+"").replace("{ssinPatient}", ssinPatient+"") + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc =>  new models.Consent(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    getTherapeuticLinks(token: string, ssinPatient: string) : Promise<Array<models.HubTherapeuticLink>|any> {
        let _body = null
        
        
        const _url = this.host+"/be_hubs/therlinks/{token}/{ssinPatient}".replace("{token}", token+"").replace("{ssinPatient}", ssinPatient+"") + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc => (doc.body as Array<JSON>).map(it=>new models.HubTherapeuticLink(it)))
                .catch(err => this.handleError(err))


    }
    getTransaction(token: string, ssinPatient: string, transactionId: string, transactionSv: string, transactionSl: string) : Promise<string|any> {
        let _body = null
        
        
        const _url = this.host+"/be_hubs/transaction/{token}/{ssinPatient}/{transactionSl}/{transactionSv}/{transactionId}".replace("{token}", token+"").replace("{ssinPatient}", ssinPatient+"").replace("{transactionId}", transactionId+"").replace("{transactionSv}", transactionSv+"").replace("{transactionSl}", transactionSl+"") + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc =>  JSON.parse(JSON.stringify(doc.body)))
                .catch(err => this.handleError(err))


    }
    getTransactionsList(token: string, ssinPatient: string, documentType?: string, from?: number, to?: number, hcPartyType?: string, inamiHcParty?: string, ssinHcParty?: string, isGlobal?: boolean) : Promise<Array<models.TransactionSummary>|any> {
        let _body = null
        
        
        const _url = this.host+"/be_hubs/transactions/{token}/{ssinPatient}".replace("{token}", token+"").replace("{ssinPatient}", ssinPatient+"") + "?ts=" + (new Date).getTime()  + (documentType ? "&documentType=" + documentType : "") + (from ? "&from=" + from : "") + (to ? "&to=" + to : "") + (hcPartyType ? "&hcPartyType=" + hcPartyType : "") + (inamiHcParty ? "&inamiHcParty=" + inamiHcParty : "") + (ssinHcParty ? "&ssinHcParty=" + ssinHcParty : "") + (isGlobal ? "&isGlobal=" + isGlobal : "")

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc => (doc.body as Array<JSON>).map(it=>new models.TransactionSummary(it)))
                .catch(err => this.handleError(err))


    }
    putPatient(token: string, idPatient: string) : Promise<models.GenericResult|any> {
        let _body = null
        
        
        const _url = this.host+"/be_hubs/patient/{token}/{idPatient}".replace("{token}", token+"").replace("{idPatient}", idPatient+"") + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('POST', _url , [], _body )
                .then(doc =>  new models.GenericResult(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    putTransaction(token: string, body?: models.SumehrTransactionBodyDto) : Promise<models.GenericResult|any> {
        let _body = null
        _body = body
        
        const _url = this.host+"/be_hubs/{token}".replace("{token}", token+"") + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('POST', _url , [], _body )
                .then(doc =>  new models.GenericResult(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    registerPatientConsent(token: string, idPatient: string) : Promise<models.GenericResult|any> {
        let _body = null
        
        
        const _url = this.host+"/be_hubs/consent/{token}/{idPatient}".replace("{token}", token+"").replace("{idPatient}", idPatient+"") + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('POST', _url , [], _body )
                .then(doc =>  new models.GenericResult(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    registerTherapeuticLink(token: string, ssinPatient: string, start?: number, comment?: string) : Promise<models.GenericResult|any> {
        let _body = null
        
        
        const _url = this.host+"/be_hubs/therlink/{token}/{ssinPatient}".replace("{token}", token+"").replace("{ssinPatient}", ssinPatient+"") + "?ts=" + (new Date).getTime()  + (start ? "&start=" + start : "") + (comment ? "&comment=" + comment : "")

        return XHR.sendCommand('POST', _url , [], _body )
                .then(doc =>  new models.GenericResult(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    updateHubId(identifier?: string, name?: string, wsdl?: string, endpoint?: string) : Promise<models.GenericResult|any> {
        let _body = null
        
        
        const _url = this.host+"/be_hubs/setup" + "?ts=" + (new Date).getTime()  + (identifier ? "&identifier=" + identifier : "") + (name ? "&name=" + name : "") + (wsdl ? "&wsdl=" + wsdl : "") + (endpoint ? "&endpoint=" + endpoint : "")

        return XHR.sendCommand('PUT', _url , [], _body )
                .then(doc =>  new models.GenericResult(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
}

