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

export class iccBeprogendaApi {
    host : string
    constructor(host) {
        this.host = host
    }


    handleError(e: XHR.Data) {
        if (e.status == 401) throw Error('auth-failed')
        else throw Error('api-error'+ e.status)
    }


    setUserCredentials(userId: string, body?: models.ProgendaCredentialsDto) : Promise<Boolean|any> {
        let _body = null
        _body = body
        
        const _url = this.host+"/be_progenda/user/{userId}/credentials".replace("{userId}", userId) + "?ts=" + (new Date).getTime() 

        return XHR.sendCommand('PUT', _url , [], _body )
                .then(doc => true)
                .catch(err => this.handleError(err))


    }
    syncSince(url: string, user: string, token: string, centerId: string, from?: number) : Promise<Array<string>|any> {
        let _body = null
        
        
        const _url = this.host+"/be_progenda/sync/{url}/{user}/{token}/{centerId}".replace("{url}", url).replace("{user}", user).replace("{token}", token).replace("{centerId}", centerId) + "?ts=" + (new Date).getTime()  + (from ? "&from=" + from : "")

        return XHR.sendCommand('POST', _url , [], _body )
                .then(doc => (doc.body as Array<JSON>).map(it=>new string(it)))
                .catch(err => this.handleError(err))


    }
}

