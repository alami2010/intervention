import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoomData, NewRoomData } from '../room-data.model';

export type PartialUpdateRoomData = Partial<IRoomData> & Pick<IRoomData, 'id'>;

export type EntityResponseType = HttpResponse<IRoomData>;
export type EntityArrayResponseType = HttpResponse<IRoomData[]>;

@Injectable({ providedIn: 'root' })
export class RoomDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/room-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(roomData: NewRoomData): Observable<EntityResponseType> {
    return this.http.post<IRoomData>(this.resourceUrl, roomData, { observe: 'response' });
  }

  update(roomData: IRoomData): Observable<EntityResponseType> {
    return this.http.put<IRoomData>(`${this.resourceUrl}/${this.getRoomDataIdentifier(roomData)}`, roomData, { observe: 'response' });
  }

  partialUpdate(roomData: PartialUpdateRoomData): Observable<EntityResponseType> {
    return this.http.patch<IRoomData>(`${this.resourceUrl}/${this.getRoomDataIdentifier(roomData)}`, roomData, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoomData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoomData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRoomDataIdentifier(roomData: Pick<IRoomData, 'id'>): number {
    return roomData.id;
  }

  compareRoomData(o1: Pick<IRoomData, 'id'> | null, o2: Pick<IRoomData, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoomDataIdentifier(o1) === this.getRoomDataIdentifier(o2) : o1 === o2;
  }

  addRoomDataToCollectionIfMissing<Type extends Pick<IRoomData, 'id'>>(
    roomDataCollection: Type[],
    ...roomDataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const roomData: Type[] = roomDataToCheck.filter(isPresent);
    if (roomData.length > 0) {
      const roomDataCollectionIdentifiers = roomDataCollection.map(roomDataItem => this.getRoomDataIdentifier(roomDataItem)!);
      const roomDataToAdd = roomData.filter(roomDataItem => {
        const roomDataIdentifier = this.getRoomDataIdentifier(roomDataItem);
        if (roomDataCollectionIdentifiers.includes(roomDataIdentifier)) {
          return false;
        }
        roomDataCollectionIdentifiers.push(roomDataIdentifier);
        return true;
      });
      return [...roomDataToAdd, ...roomDataCollection];
    }
    return roomDataCollection;
  }
}
