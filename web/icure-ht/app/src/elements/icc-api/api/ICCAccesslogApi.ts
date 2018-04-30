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

export class iccAccesslogApi {
    host : string
    constructor(host: string) {
        this.host = host
    }


    handleError(e: XHR.Data) {
        if (e.status == 401) throw Error('auth-failed')
        else throw Error('api-error'+ e.status)
    }


    createAccessLog(body?: models.AccessLogDto) : Promise<models.AccessLogDto|any> {
        let _body = null
        _body = body
        
        const _url = this.host+"/accesslog" + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('POST', _url , [], _body )
                .then(doc =>  new models.AccessLogDto(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    deleteAccessLog(accessLogIds: string) : Promise<Boolean|any> {
        let _body = null
        
        
        const _url = this.host+"/accesslog/{accessLogIds}".replace("{accessLogIds}", accessLogIds+"") + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('DELETE', _url , [], _body )
                .then(doc => true)
                .catch(err => this.handleError(err))


    }
    findByUserAfterDate(userId: string, accessType?: string, startDate?: number, startKey?: string, startDocumentId?: string, limit?: number, descending?: boolean) : Promise<models.AccessLogPaginatedList|any> {
        let _body = null
        
        
        const _url = this.host+"/accesslog/byUser" + "?ts=" + (new Date).getTime()  + (userId ? "&userId=" + userId : "") + (accessType ? "&accessType=" + accessType : "") + (startDate ? "&startDate=" + startDate : "") + (startKey ? "&startKey=" + startKey : "") + (startDocumentId ? "&startDocumentId=" + startDocumentId : "") + (limit ? "&limit=" + limit : "") + (descending ? "&descending=" + descending : "")

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc =>  new models.AccessLogPaginatedList(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    getAccessLog(accessLogId: string) : Promise<models.AccessLogDto|any> {
        let _body = null
        
        
        const _url = this.host+"/accesslog/{accessLogId}".replace("{accessLogId}", accessLogId+"") + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc =>  new models.AccessLogDto(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
    listAccessLogs(startKey?: string, startDocumentId?: string, limit?: string) : Promise<Array<models.AccessLogDto>|any> {
        let _body = null
        
        
        const _url = this.host+"/accesslog" + "?ts=" + (new Date).getTime()  + (startKey ? "&startKey=" + startKey : "") + (startDocumentId ? "&startDocumentId=" + startDocumentId : "") + (limit ? "&limit=" + limit : "")

        return XHR.sendCommand('GET', _url , [], _body )
                .then(doc => (doc.body as Array<JSON>).map(it=>new models.AccessLogDto(it)))
                .catch(err => this.handleError(err))


    }
    modifyAccessLog(body?: models.AccessLogDto) : Promise<models.AccessLogDto|any> {
        let _body = null
        _body = body
        
        const _url = this.host+"/accesslog" + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('PUT', _url , [], _body )
                .then(doc =>  new models.AccessLogDto(doc.body as JSON))
                .catch(err => this.handleError(err))


    }
}

