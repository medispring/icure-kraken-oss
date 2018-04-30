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

export class iccEntitytemplateApi {
    host : string
    constructor(host: string) {
        this.host = host
    }


    handleError(e: XHR.Data) {
        if (e.status == 401) throw Error('auth-failed')
        else throw Error('api-error'+ e.status)
    }


    createEntityTemplate(body?: models.EntityTemplateDto) : Promise<models.EntityTemplateDto|any> {
        let _body = null
        _body = body
        
        const _url = this.host+"/entitytemplate" + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('POST', _url , [], _body )
                .then(doc =>  new models.EntityTemplateDto(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    findEntityTemplates(userId: string, type: string, searchString?: string, includeEntities?: boolean) : Promise<Array<models.EntityTemplateDto>|any> {
        let _body = null
        
        
        const _url = this.host+"/entitytemplate/find/{userId}/{type}".replace("{userId}", userId+"").replace("{type}", type+"") + "?ts=" + (new Date).getTime()  + (searchString ? "&searchString=" + searchString : "") + (includeEntities ? "&includeEntities=" + includeEntities : "")

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc => (doc.body as Array<JSON>).map(it=>new models.EntityTemplateDto(it)))
                .catch(err => this.handleError(err))


    }
    getEntityTemplate(entityTemplateId: string) : Promise<models.EntityTemplateDto|any> {
        let _body = null
        
        
        const _url = this.host+"/entitytemplate/{entityTemplateId}".replace("{entityTemplateId}", entityTemplateId+"") + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc =>  new models.EntityTemplateDto(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    getEntityTemplates(entityTemplateIds: string) : Promise<Array<models.EntityTemplateDto>|any> {
        let _body = null
        
        
        const _url = this.host+"/entitytemplate/byIds/{entityTemplateIds}".replace("{entityTemplateIds}", entityTemplateIds+"") + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc => (doc.body as Array<JSON>).map(it=>new models.EntityTemplateDto(it)))
                .catch(err => this.handleError(err))


    }
    modifyEntityTemplate(body?: models.EntityTemplateDto) : Promise<models.EntityTemplateDto|any> {
        let _body = null
        _body = body
        
        const _url = this.host+"/entitytemplate" + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('PUT', _url , [], _body )
                .then(doc =>  new models.EntityTemplateDto(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
}

