import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIntervention } from '../intervention.model';
import { InterventionService } from '../service/intervention.service';

@Injectable({ providedIn: 'root' })
export class InterventionRoutingResolveService implements Resolve<IIntervention | null> {
  constructor(protected service: InterventionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIntervention | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((intervention: HttpResponse<IIntervention>) => {
          if (intervention.body) {
            return of(intervention.body);
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
