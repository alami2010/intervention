import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFloorData, NewFloorData } from '../floor-data.model';

export type PartialUpdateFloorData = Partial<IFloorData> & Pick<IFloorData, 'id'>;

export type EntityResponseType = HttpResponse<IFloorData>;
export type EntityArrayResponseType = HttpResponse<IFloorData[]>;

@Injectable({ providedIn: 'root' })
export class FloorDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/floor-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(floorData: NewFloorData): Observable<EntityResponseType> {
    return this.http.post<IFloorData>(this.resourceUrl, floorData, { observe: 'response' });
  }

  update(floorData: IFloorData): Observable<EntityResponseType> {
    return this.http.put<IFloorData>(`${this.resourceUrl}/${this.getFloorDataIdentifier(floorData)}`, floorData, { observe: 'response' });
  }

  partialUpdate(floorData: PartialUpdateFloorData): Observable<EntityResponseType> {
    return this.http.patch<IFloorData>(`${this.resourceUrl}/${this.getFloorDataIdentifier(floorData)}`, floorData, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFloorData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFloorData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFloorDataIdentifier(floorData: Pick<IFloorData, 'id'>): number {
    return floorData.id;
  }

  compareFloorData(o1: Pick<IFloorData, 'id'> | null, o2: Pick<IFloorData, 'id'> | null): boolean {
    return o1 && o2 ? this.getFloorDataIdentifier(o1) === this.getFloorDataIdentifier(o2) : o1 === o2;
  }

  addFloorDataToCollectionIfMissing<Type extends Pick<IFloorData, 'id'>>(
    floorDataCollection: Type[],
    ...floorDataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const floorData: Type[] = floorDataToCheck.filter(isPresent);
    if (floorData.length > 0) {
      const floorDataCollectionIdentifiers = floorDataCollection.map(floorDataItem => this.getFloorDataIdentifier(floorDataItem)!);
      const floorDataToAdd = floorData.filter(floorDataItem => {
        const floorDataIdentifier = this.getFloorDataIdentifier(floorDataItem);
        if (floorDataCollectionIdentifiers.includes(floorDataIdentifier)) {
          return false;
        }
        floorDataCollectionIdentifiers.push(floorDataIdentifier);
        return true;
      });
      return [...floorDataToAdd, ...floorDataCollection];
    }
    return floorDataCollection;
  }
}
