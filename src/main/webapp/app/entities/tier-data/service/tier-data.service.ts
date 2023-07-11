import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITierData, NewTierData } from '../tier-data.model';

export type PartialUpdateTierData = Partial<ITierData> & Pick<ITierData, 'id'>;

export type EntityResponseType = HttpResponse<ITierData>;
export type EntityArrayResponseType = HttpResponse<ITierData[]>;

@Injectable({ providedIn: 'root' })
export class TierDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tier-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tierData: NewTierData): Observable<EntityResponseType> {
    return this.http.post<ITierData>(this.resourceUrl, tierData, { observe: 'response' });
  }

  update(tierData: ITierData): Observable<EntityResponseType> {
    return this.http.put<ITierData>(`${this.resourceUrl}/${this.getTierDataIdentifier(tierData)}`, tierData, { observe: 'response' });
  }

  partialUpdate(tierData: PartialUpdateTierData): Observable<EntityResponseType> {
    return this.http.patch<ITierData>(`${this.resourceUrl}/${this.getTierDataIdentifier(tierData)}`, tierData, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITierData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITierData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTierDataIdentifier(tierData: Pick<ITierData, 'id'>): number {
    return tierData.id;
  }

  compareTierData(o1: Pick<ITierData, 'id'> | null, o2: Pick<ITierData, 'id'> | null): boolean {
    return o1 && o2 ? this.getTierDataIdentifier(o1) === this.getTierDataIdentifier(o2) : o1 === o2;
  }

  addTierDataToCollectionIfMissing<Type extends Pick<ITierData, 'id'>>(
    tierDataCollection: Type[],
    ...tierDataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tierData: Type[] = tierDataToCheck.filter(isPresent);
    if (tierData.length > 0) {
      const tierDataCollectionIdentifiers = tierDataCollection.map(tierDataItem => this.getTierDataIdentifier(tierDataItem)!);
      const tierDataToAdd = tierData.filter(tierDataItem => {
        const tierDataIdentifier = this.getTierDataIdentifier(tierDataItem);
        if (tierDataCollectionIdentifiers.includes(tierDataIdentifier)) {
          return false;
        }
        tierDataCollectionIdentifiers.push(tierDataIdentifier);
        return true;
      });
      return [...tierDataToAdd, ...tierDataCollection];
    }
    return tierDataCollection;
  }
}
