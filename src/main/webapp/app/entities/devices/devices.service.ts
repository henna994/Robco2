import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDevices } from 'app/shared/model/devices.model';

type EntityResponseType = HttpResponse<IDevices>;
type EntityArrayResponseType = HttpResponse<IDevices[]>;

@Injectable({ providedIn: 'root' })
export class DevicesService {
    public resourceUrl = SERVER_API_URL + 'api/devices';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/devices';

    constructor(protected http: HttpClient) {}

    create(devices: IDevices): Observable<EntityResponseType> {
        return this.http.post<IDevices>(this.resourceUrl, devices, { observe: 'response' });
    }

    update(devices: IDevices): Observable<EntityResponseType> {
        return this.http.put<IDevices>(this.resourceUrl, devices, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDevices>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDevices[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDevices[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
