import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITierData } from '../tier-data.model';
import { TierDataService } from '../service/tier-data.service';

@Injectable({ providedIn: 'root' })
export class TierDataRoutingResolveService implements Resolve<ITierData | null> {
  constructor(protected service: TierDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITierData | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tierData: HttpResponse<ITierData>) => {
          if (tierData.body) {
            return of(tierData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
