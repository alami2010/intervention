import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITier, NewTier } from '../tier.model';

export type PartialUpdateTier = Partial<ITier> & Pick<ITier, 'id'>;

export type EntityResponseType = HttpResponse<ITier>;
export type EntityArrayResponseType = HttpResponse<ITier[]>;

@Injectable({ providedIn: 'root' })
export class TierService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tiers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tier: NewTier): Observable<EntityResponseType> {
    return this.http.post<ITier>(this.resourceUrl, tier, { observe: 'response' });
  }

  update(tier: ITier): Observable<EntityResponseType> {
    return this.http.put<ITier>(`${this.resourceUrl}/${this.getTierIdentifier(tier)}`, tier, { observe: 'response' });
  }

  partialUpdate(tier: PartialUpdateTier): Observable<EntityResponseType> {
    return this.http.patch<ITier>(`${this.resourceUrl}/${this.getTierIdentifier(tier)}`, tier, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTierIdentifier(tier: Pick<ITier, 'id'>): number {
    return tier.id;
  }

  compareTier(o1: Pick<ITier, 'id'> | null, o2: Pick<ITier, 'id'> | null): boolean {
    return o1 && o2 ? this.getTierIdentifier(o1) === this.getTierIdentifier(o2) : o1 === o2;
  }

  addTierToCollectionIfMissing<Type extends Pick<ITier, 'id'>>(
    tierCollection: Type[],
    ...tiersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tiers: Type[] = tiersToCheck.filter(isPresent);
    if (tiers.length > 0) {
      const tierCollectionIdentifiers = tierCollection.map(tierItem => this.getTierIdentifier(tierItem)!);
      const tiersToAdd = tiers.filter(tierItem => {
        const tierIdentifier = this.getTierIdentifier(tierItem);
        if (tierCollectionIdentifiers.includes(tierIdentifier)) {
          return false;
        }
        tierCollectionIdentifiers.push(tierIdentifier);
        return true;
      });
      return [...tiersToAdd, ...tierCollection];
    }
    return tierCollection;
  }
}
