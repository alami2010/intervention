import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFloorData } from '../floor-data.model';
import { FloorDataService } from '../service/floor-data.service';

@Injectable({ providedIn: 'root' })
export class FloorDataRoutingResolveService implements Resolve<IFloorData | null> {
  constructor(protected service: FloorDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFloorData | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((floorData: HttpResponse<IFloorData>) => {
          if (floorData.body) {
            return of(floorData.body);
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
