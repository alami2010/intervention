import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITier } from '../tier.model';
import { TierService } from '../service/tier.service';

@Injectable({ providedIn: 'root' })
export class TierRoutingResolveService implements Resolve<ITier | null> {
  constructor(protected service: TierService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITier | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tier: HttpResponse<ITier>) => {
          if (tier.body) {
            return of(tier.body);
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
